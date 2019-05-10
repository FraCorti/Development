package it.colletta.repository.phrase;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.mongodb.client.result.UpdateResult;

import it.colletta.model.PhraseModel;
import it.colletta.model.SolutionModel;
import it.colletta.model.helper.FilterHelper;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class PhraseRepositoryImpl implements PhraseCustomQueryInterface {

  private MongoTemplate mongoTemplate;

  @Autowired
  public PhraseRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<PhraseModel> findAllByAuthor(final String authorId) {
    Query query = new Query(Criteria.where("solutions.authorId").is(authorId));
    final List<PhraseModel> phraseModels = mongoTemplate.find(query, PhraseModel.class);
    return phraseModels;
  }

  @Override
  public List<SolutionModel> findAllSolutionsByAuthor(final String authorId) {
    Query query = new Query(Criteria.where("solutions.authorId").is(authorId));
    query.fields().include("solutions");
    return mongoTemplate.find(query, SolutionModel.class);
  }

  @Override
  public UpdateResult increaseReliability(SolutionModel solutionModel) {
    Query query =
        new Query(Criteria.where("solutions.solutionText").is(solutionModel.getSolutionText()));
    Update update = new Update().inc("solutions.$.reliability", 1);
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, PhraseModel.class);
    return updateResult;
  }

  @Override
  public SolutionModel getSolution(final String phraseId, String solutionId) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(Criteria.where("_id").is(new ObjectId(phraseId))), unwind("$solutions"),
        match(Criteria.where("solutions._id").in(new ObjectId(solutionId))), limit(1));
    AggregationResults<Document> doc =
        mongoTemplate.aggregate(aggregation, "phrases", Document.class);
    Document map = Objects.requireNonNull(doc.getUniqueMappedResult());
    Document obj = map.get("solutions", Document.class);
    final SolutionModel build = SolutionModel.builder().id(obj.getObjectId("_id").toHexString())
        .solutionText(obj.getString("solutionText")).authorId(obj.getString("authorId"))
        .reliability(obj.getInteger("reliability")).dateSolution(obj.getLong("dateSolution"))
        .build();
    return build;
  }

  @Override
  public List<Document> findAllPhrases() {
    Aggregation aggregation = Aggregation.newAggregation(unwind("$solutions"),
        match(Criteria.where("solutions.visibilityDev").not().in(false)));

    AggregationResults<Document> doc =
        mongoTemplate.aggregate(aggregation, "phrases", Document.class);
    return doc.getMappedResults();
  }

  @Override
  public List<Document> findAllPhrasesWithFilter(FilterHelper filterHelper) {

    final long oneDay = 86400000;
    long dateStart = filterHelper.getStartDate() != null ? filterHelper.getStartDate() : 0;
    GregorianCalendar start = new GregorianCalendar();
    start.setTimeInMillis(dateStart);
    start.set(GregorianCalendar.HOUR, 0);
    start.set(GregorianCalendar.MINUTE, 0);
    start.set(GregorianCalendar.SECOND, 0);
    long dateEnd = filterHelper.getEndDate() != null ? filterHelper.getEndDate() + oneDay
        : new Date().getTime() + oneDay;
    GregorianCalendar end = new GregorianCalendar();
    end.setTimeInMillis(dateEnd);
    end.set(GregorianCalendar.HOUR, 0);
    end.set(GregorianCalendar.MINUTE, 0);
    end.set(GregorianCalendar.SECOND, 0);

    Aggregation aggregation = Aggregation.newAggregation(unwind("$solutions"),
        match(Criteria.where("language").in(filterHelper.getLanguages())),
        match(Criteria.where("solutions.dateSolution").gte(start.getTimeInMillis())
            .lte(end.getTimeInMillis())),
        match(Criteria.where("solutions.reliability").gte(filterHelper.getMinReliability())),
        match(Criteria.where("solutions.visibilityDev").not().in(false)));

    AggregationResults<Document> doc =
        mongoTemplate.aggregate(aggregation, "phrases", Document.class);
    return doc.getMappedResults();
  }

}
