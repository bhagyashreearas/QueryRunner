package queryrunner;

/**
 * QueryData
 *
 * @author Bhagyashree Aras
 */
public class QueryData {

    
    QueryData(String query, String[] parms, boolean [] likeparms,
              boolean isAction, boolean isParm)
    {
        m_queryString = query;
        m_arrayParms = parms;
        m_arrayLikeParms = likeparms;
        m_isAction = isAction;
        m_isParms = isParm;        
    }

    
    String GetQueryString()
    {
        return m_queryString;
    }
    
    int GetParmAmount()
    {
        if (m_arrayParms == null)
            return 0;
        else
            return m_arrayParms.length;
    }
    
  
    String GetParamText(int index)
    {
        return m_arrayParms[index];
    }
    
    boolean GetLikeParam(int index)
    {
        return m_arrayLikeParms[index];
    }
    
    boolean [] GetAllLikeParams()
    {
        return m_arrayLikeParms;
    }
    
    boolean IsQueryAction()
    {
        return m_isAction;
    }
    
    boolean IsQueryParm()
    {
        return m_isParms;
    }
     
    private String m_queryString;
    private String [] m_arrayParms;
    private boolean m_isAction;
    private boolean m_isParms;   
    private boolean [] m_arrayLikeParms;
}
