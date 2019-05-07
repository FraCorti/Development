const initState = {
  devList: [],
  usersList: [],
  filter: {
    reliability: 0,
    dateMin: '',
    dateMax: '',
    openFilters: false
  }
};

// penso che case 'INIT_STATE' sia inutile e non venga mai utilizzato
const AdminReducer = (state = initState, action) => {
  switch (action.type) {
    case 'INIT_STATE':
      return {
        ...initState
      };
    case 'UPDATE_FILTER':
      return {
        ...state,
        filter: {
          ...state.filter,
          ...action.filter
        }
      };
    case 'UPDATE_DEV_LIST': {
      return {
        ...state,
        devList: action.payload
      };
    }
    case 'UPDATE_USER_LIST': {
      return {
        ...state,
        usersList: action.payload
      };
    }
    case 'USER_DELETE_SUCCES': {
      const { usernameOrId } = action.payload;
      const { usersList, devList } = state;
      const indexUser = usersList.findIndex(user => user.id === usernameOrId);
      if (indexUser > -1) usersList.splice(indexUser, 1);

      const indexDev = devList.findIndex(dev => dev.id === usernameOrId);
      if (indexDev > -1) devList.splice(indexDev, 1);

      return {
        ...state,
        usersList,
        devList
      };
    }
    case 'DEV_APPROVE_SUCCES': {
      const { usernameOrId } = action.payload;
      const { devList } = state;
      // ritorna l’indice dell’elemento che ha id=== usernameOrld || se non c’è ritorna -1
      const indexDev = devList.findIndex(dev => dev.id === usernameOrId);
      if (indexDev > -1) devList.splice(indexDev, 1);
      return {
        ...state,
        devList
      };
    }
    default: {
      return { ...state, loader: false };
    }
  }
};

export default AdminReducer;
