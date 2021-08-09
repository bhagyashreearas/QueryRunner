package queryrunner;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * QueryRunner takes a list of Queries that are initialized in it's constructor
 * and provides functions that will call the various functions in the QueryJDBC class
 * which will enable MYSQL queries to be executed. It also has functions to provide the
 * returned data from the Queries. Currently the eventHandlers in QueryFrame call these
 * functions in order to run the Queries.
 *
 * @author Bhagyashree Aras
 */
public class QueryRunner
{
    public QueryRunner(String[] array)
    {
        this.m_jdbcData = new QueryJDBC();
        m_updateAmount = 0;
        m_queryArray = new ArrayList<>();
        m_error="";


        this.m_projectTeamApplication="CRYSTAL GAZING";

        // gets a random planet horoscope based on the user's planet_id
        m_queryArray.add(new QueryData(array[0],
                new String [] {"PLANET_ID"}, new boolean [] {false}, false, true));

        // gets a random star horoscope based on the user's star_id
        m_queryArray.add(new QueryData(array[1],
                new String [] {"STAR_ID"}, new boolean [] {false}, false,
                true));

        // Displays planet information for each user
        m_queryArray.add(new QueryData(array[2], null, null, false, false));

        // get count
        m_queryArray.add(new QueryData(array[3], null, null, false, false));

        // get all user data
        m_queryArray.add(new QueryData(array[4], null, null, false, false));

        // get all planet descriptions for a certain planet_id
        m_queryArray.add(new QueryData(array[5], new String [] {"PLANET_ID"},
                new boolean [] {false},  false, true));

        // get all users whose first name has a certain pattern to it
        m_queryArray.add(new QueryData(array[6], new String [] {
                "USER_FIRST_NAME"}, new boolean [] {true}, false, true));

        // inserts a new planet_horoscope_description for a certain planet_id
        // NOTE: must make sure planet_horoscope_id is unique
        m_queryArray.add(new QueryData(array[7] ,new String [] {
                "PLANET_HOROSCOPE_ID", "PLANET_ID", "PLANET_HOROSCOPE_DESCRIPTION"}, new boolean [] {false, false, false}, true, true));
    }

    public int GetTotalQueries()
    {
        return m_queryArray.size();
    }

    public int GetParameterAmtForQuery(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetParmAmount();
    }

    public String GetParamText(int queryChoice, int parmnum )
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetParamText(parmnum);
    }

    public String GetQueryText(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetQueryString();
    }

    /**
     * Function will return how many rows were updated as a result
     * of the update query
     *
     * @return Returns how many rows were updated
     */

    public int GetUpdateAmount()
    {
        return m_updateAmount;
    }

    /**
     * Function will return ALL of the Column Headers from the query
     *
     * @return Returns array of column headers
     */
    public String [] GetQueryHeaders()
    {
        return m_jdbcData.GetHeaders();
    }

    /**
     * After the query has been run, all of the data has been captured into
     * a multi-dimensional string array which contains all the row's. For each
     * row it also has all the column data. It is in string format
     *
     * @return multi-dimensional array of String data based on the resultset
     * from the query
     */
    public String[][] GetQueryData()
    {
        return m_jdbcData.GetData();
    }

    public String GetProjectTeamApplication()
    {
        return m_projectTeamApplication;
    }
    public boolean  isActionQuery (int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.IsQueryAction();
    }

    public boolean isParameterQuery(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.IsQueryParm();
    }


    public boolean ExecuteQuery(int queryChoice, String [] parms)
    {
        boolean bOK = true;
        QueryData e=m_queryArray.get(queryChoice);
        bOK = m_jdbcData.ExecuteQuery(e.GetQueryString(), parms, e.GetAllLikeParams());
        return bOK;
    }

    public boolean ExecuteUpdate(int queryChoice, String [] parms)
    {
        boolean bOK = true;
        QueryData e=m_queryArray.get(queryChoice);
        bOK = m_jdbcData.ExecuteUpdate(e.GetQueryString(), parms);
        m_updateAmount = m_jdbcData.GetUpdateCount();
        return bOK;
    }


    public boolean Connect(String szHost, String szUser, String szPass, String szDatabase)
    {

        boolean bConnect = m_jdbcData.ConnectToDatabase(szHost, szUser, szPass, szDatabase);
        if (bConnect == false)
            m_error = m_jdbcData.GetError();
        return bConnect;
    }

    public boolean Disconnect()
    {
        // Disconnect the JDBCData Object
        boolean bConnect = m_jdbcData.CloseDatabase();
        if (bConnect == false)
            m_error = m_jdbcData.GetError();
        return true;
    }

    public String GetError()
    {
        return m_error;
    }

    public String spaces(String word)
    {
        int stringSpaces = 0;
        int s = word.length();
        stringSpaces = 35 - s;
        String str = "";
        for (int i = 0; i < stringSpaces; i++) {
            str += " ";
        }
        return str;
    }

    private QueryJDBC m_jdbcData;
    private String m_error;
    private String m_projectTeamApplication;
    private ArrayList<QueryData> m_queryArray;
    private int m_updateAmount;

    public static void main(String[] args) throws FileNotFoundException {
        console(args);
    }

    private static void console(String[] args)throws FileNotFoundException
    {
        int size = 9;
        String array[] = new String[size];
        readFromFile(array);

        // put -console in argument so it would be called in main
        // run edit configurations(-console)
        String userInput;
        String stringQueries;
        int numQueries;
        final QueryRunner queryrunner = new QueryRunner(array);

        if (args.length == 0)
        {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {

                    new QueryFrame(queryrunner).setVisible(true);
                }
            });
        }
        else
        {
            if (args[0].equals ("-console"))
            {
                // connect to the database
                Scanner myScan = new Scanner(System.in);
                System.out.println("Enter host name: ");
                String hostName = myScan.nextLine();
                System.out.println("Enter username: ");
                String userName = myScan.nextLine();
                System.out.println("Enter password: ");
                String password = myScan.nextLine();
                System.out.println("Enter database: ");
                String database = myScan.nextLine();
                boolean connect = queryrunner.Connect(hostName,userName,password,database);
                if (connect) {
                    // let user know if they have successfully connected the database
                    System.out.println();
                    System.out.println("Yay! You are now connected to the " + database + " database.");
                    System.out.println();
                }


                do {
                    System.out.println("How many queries do you want to " +
                            "run?");
                    stringQueries = myScan.nextLine();
                    numQueries = Integer.parseInt(stringQueries);
                    //System.out.println(numQueries + numQueries);

                    // for each query fo the following
                    for (int query = 1; query <= numQueries; query++) {
                        // ask the user which query they want from a list of numbered available queries
                        System.out.println("Which query do you want?");
                        for (int queryChoice = 0; queryChoice < queryrunner.GetTotalQueries(); queryChoice++) {
                            System.out.println((queryChoice + 1) + ") " + queryrunner.GetQueryText(queryChoice));
                        }
                        String stringQueryChoice = myScan.nextLine();
                        int queryChoice = Integer.parseInt(stringQueryChoice) - 1; // save their query num choice

                        // declare and initialize an empty string of parameters
                        String[] params = {};
                        // if the user's current queryChoice has parameters, then set the number of parameters
                        // to the parameter amount for that query
                        if (queryrunner.isParameterQuery(queryChoice)) {
                            params = new String[queryrunner.GetParameterAmtForQuery(queryChoice)];

                            // for each parameter in the params array, get the parameter label and print out to the console
                            // then get users entry for that parameter
                            for (int parmnum = 0; parmnum < params.length; parmnum++) {
                                String paramLabel = queryrunner.GetParamText(queryChoice, parmnum);
                                System.out.println("Param for: " + paramLabel + "?");
                                String paramVal = myScan.nextLine();
                                params[parmnum] = paramVal;
                            }
                        }
                        // if the query is an action query
                        if (queryrunner.isActionQuery(queryChoice)) {
                            // call ExecuteUpdate to run the Query
                            queryrunner.ExecuteUpdate(queryChoice, params);
                            // call GetUpdateAmount to find out how many rows were affected and print that value
                            System.out.println("Number of rows affected: " + queryrunner.GetUpdateAmount());

                        } else { // if the query is not an action query
                            queryrunner.ExecuteQuery(queryChoice, params);

                            // get the data returned for the query, save it in 2-D array of string values
                            String[][] values = queryrunner.GetQueryData();

                            // print out all of the results from the 2-D array
                            for (String[] value : values) { // for each row in the values array
                                for (String s : value) { // for each column in the values array
                                    System.out.print(s + queryrunner.spaces(s)); // print the column value with a space
                                }
                                System.out.println(); // print a new line before getting to the next row
                            }
                        }
                    }
                    do {
                        System.out.println("Do you want to exit? (if yes," +
                                " type exit or type no)");
                        userInput = myScan.nextLine();

                        if (userInput.compareTo("exit") == 0) {
                            System.out.println("Thank you for using the query " +
                                    "runner!");
                            System.exit(0);
                        }

                    } while (userInput.compareTo("no") > 0 ||
                            userInput.compareTo("no") < 0 );

                } while (userInput.compareTo("no") == 0);

                //disconnect queryrunner;
                boolean disconnect= queryrunner.Disconnect();
                if (disconnect) {
                    System.out.println();
                    System.out.println("Successfully Disconnected");
                }

            }
        }
    }


    private static void readFromFile(String[] array)throws FileNotFoundException
    {
        final String ADDRESS = "queries.txt";
        int count = 0;
        String line;

        try {
            File file = new File(ADDRESS);
            FileReader fileReader = new FileReader(file);
            BufferedReader  bufferedReader= new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            array[count] = line;
            while (line != null) {
                count++;
                line = bufferedReader.readLine();
                array[count] = line;

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e){
            System.out.println("Incorrect File");
            e.printStackTrace();
        }
    }
}
