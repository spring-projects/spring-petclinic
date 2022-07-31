package org.springframework.samples.petclinic.ImpactAnalyzer.connect;

import org.springframework.samples.petclinic.ImpactAnalyzer.models.HTMLElement;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.Position;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.TestElement;

import java.sql.*;
import java.util.ArrayList;


public class Connect {
    static Connection conn = null;
    /**
     * connect.Connect to a sample database
     */

    public static boolean RESET_TABLES = false;
    public static void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:identifier.sqlite";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            if(RESET_TABLES) {
                String DropTableQuery = "DROP TABLE IF EXISTS TestElementPageRelation;"
						+"DROP TABLE IF EXISTS JavaTestElements;"+
                        "DROP TABLE IF EXISTS Page;"+
                        "DROP TABLE IF EXISTS Version;"+
                        "DROP TABLE IF EXISTS Commits;"+
                        "DROP TABLE IF EXISTS AccessMethods;"+
                        "DROP TABLE IF EXISTS ActionMethods;"+
                        "DROP TABLE IF EXISTS ElementDependencies;"+
                        "DROP TABLE IF EXISTS HtmlElements;";
                stmt.executeUpdate(DropTableQuery);
            }
            String sql_version = "CREATE TABLE IF NOT EXISTS Version " +

                    "(v_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    "v_time Timestamp)";

            stmt.executeUpdate(sql_version);



            String sql_page = "CREATE TABLE IF NOT EXISTS Page " +

                    "(p_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    "p_page_name TEXT," +

                    "p_version_id int," +

                    "UNIQUE(p_page_name,p_version_id),"+

                    "FOREIGN KEY (p_version_id) REFERENCES Version(v_id))";

            stmt.executeUpdate(sql_page);

            String sql_commits = "CREATE TABLE IF NOT EXISTS Commits " +

                    "(c_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " c_text TEXT,"+

                    "c_htmlPage TEXT)";

            stmt.executeUpdate(sql_commits);

            String sql_accessMethods = "CREATE TABLE IF NOT EXISTS AccessMethods " +

                    "(a_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    "a_access_method_name TEXT," +

                    "UNIQUE(a_access_method_name))";

            stmt.executeUpdate(sql_accessMethods);

            String sql_actionMethods = "CREATE TABLE IF NOT EXISTS ActionMethods " +

                    "(ac_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    "ac_action_method_name TEXT," +

                    "UNIQUE(ac_action_method_name))";

            stmt.executeUpdate(sql_actionMethods);


            String sql = "CREATE TABLE IF NOT EXISTS HtmlElements " +

                    "(h_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " h_page_name_id int, " +

                    " h_element_id int, " +

                    " h_value TEXT," + //Doesn't exists in JSON

                    " h_xpath TEXT NOT NULL," +

                    " h_type TEXT," + //Doesn't exists in JSON

                    " h_name TEXT," +

                    " h_class_name TEXT, " +

                    " h_css_selector TEXT,"+

                    " h_tag TEXT,"+

                    "FOREIGN KEY (h_page_name_id) REFERENCES Page(p_id))";


            stmt.executeUpdate(sql);

            String sql2 = "CREATE TABLE IF NOT EXISTS JavaTestElements " +

                    "(t_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " t_access_method_id int, " +

                    " t_access_method_value TEXT NOT NULL, " +

                    " t_action_method_id int, " +

                    " t_action_method_value TEXT," +

                    "FOREIGN KEY (t_access_method_id) REFERENCES AccessMethods(a_id),"+

                    "FOREIGN KEY (t_action_method_id) REFERENCES ActionMethods(ac_id))";

            stmt.executeUpdate(sql2);

			String sql_testElementPageRelation = "CREATE TABLE IF NOT EXISTS TestElementPageRelation " +

				"(pr_id INTEGER PRIMARY KEY AUTOINCREMENT," +

				" pr_java_test_elements_id int, "+

				"pr_page_id TEXT,"+

				"pr_positon int,"+

				"UNIQUE(pr_java_test_elements_id, pr_page_id, pr_positon),"+

				"FOREIGN KEY (pr_page_id) REFERENCES Page(p_id)"+

				"FOREIGN KEY (pr_java_test_elements_id) REFERENCES JavaTestElements(t_id))";

			stmt.executeUpdate(sql_testElementPageRelation);

            String sql_elementDependencies = "CREATE TABLE IF NOT EXISTS ElementDependencies " +

                    "(d_id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " d_java_test_elements_id int, " +

                    " d_html_elements_id int, " +

                    " d_class_name TEXT, " +

                    " d_test_name TEXT," +

                    " d_position int," +

                    "FOREIGN KEY (d_java_test_elements_id) REFERENCES JavaTestElements(t_id),"+

                    "FOREIGN KEY (d_html_elements_id) REFERENCES HtmlElements(h_id))";

            stmt.executeUpdate(sql_elementDependencies);

            String dummy_table = "CREATE TABLE IF NOT EXISTS DummyTable " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " text TEXT,"+

                    "page TEXT)";

            stmt.executeUpdate(dummy_table);

            stmt.close();
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
        }
    }

    public static int getIdAndOrAddAccessMethod(String accessMethod) {
        int result = 0;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO AccessMethods (a_access_method_name) VALUES (?);"
                            + "SELECT a_id FROM AccessMethods WHERE a_access_method_name = ?;");
            stmt.setString(1, accessMethod);
            stmt.setString(2, accessMethod);
            stmt.executeUpdate();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getMaxHid() {
        int hid = 0;
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(h_id) FROM HtmlElements;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hid = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return hid;
    }
    public static int getMaxTid() {
        int tid = 0;
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(t_id) FROM JavaTestElements;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tid = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tid;
    }
//	public static int getTid() {
//		int tid = 0;
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("SELECT t_id FROM JavaTestElements join AccessMethods AM on JavaTestElements.t_access_method_id = AM.a_id\n" +
//				"where AM.a_access_method_name == ? AND t_access_method_value == ?;");
//			ResultSet rs = stmt.executeQuery();
//			while (rs.next()) {
//				tid = rs.getInt(1);
//			}
//			rs.close();
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//		return tid;
//	}
    public static void insert(String query){
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }catch (SQLException e){
            e.printStackTrace();

            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<String> executeString(String query) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
        }
        return result;
    }

    public static ArrayList<Integer> execute(String query) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
        }
        return result;
    }
    public static ArrayList<HTMLElement> listAllHtmlElements() {
        ArrayList<HTMLElement> info = new ArrayList<HTMLElement>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HTMLElement htmlElement = new HTMLElement();
                htmlElement.setUrl(rs.getString("p_page_name"));
                htmlElement.setId(rs.getString("h_element_id"));
                htmlElement.setValue(rs.getString("h_value"));
                htmlElement.setXpath(rs.getString("h_xpath"));
                //"TypeNotDone"
                htmlElement.setName(rs.getString("h_name"));
                htmlElement.setClassName(rs.getString("h_class_name"));
                htmlElement.setTag(rs.getString("h_tag"));
                //"cssSelectorNotDone"
                info.add(htmlElement);
                //System.out.println("ID of HtmlElement: "+rs.getInt("h_id"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return info;
    }
    public static ArrayList<String> listAllJavaTestElementWithAccessMethod(String accessMethodName) {
        ArrayList<String> info = new ArrayList<String>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JavaTestElements t1 LEFT OUTER JOIN AccessMethods t2 ON t1.t_access_method_id == t2.a_id WHERE t2.a_access_method_name == ?;");
            stmt.setString(1, accessMethodName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                info.add(rs.getString("t_access_method_value"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return info;
    }
    public static int getIdOfAllJavaTestElementWithAccessMethodAndValue(String accessMethodName, String accessMethodValue) {
//        ArrayList<Integer> info = new ArrayList<Integer>();
        int info = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT t1.t_id FROM JavaTestElements t1 LEFT OUTER JOIN AccessMethods t2 ON t1.t_access_method_id == t2.a_id WHERE t2.a_access_method_name == ? AND t1.t_access_method_value == ?;");
            stmt.setString(1, accessMethodName);
            stmt.setString(2, accessMethodValue);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
//                info.add(rs.getInt("t_id"));
                info = rs.getInt("t_id");
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return info;
    }

	public static  ArrayList<Integer> getIdOfAllJavaTestElement(String accessMethodName, String accessMethodValue) {
        ArrayList<Integer> info = new ArrayList<Integer>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT t1.t_id FROM JavaTestElements t1 LEFT OUTER JOIN AccessMethods t2 ON t1.t_access_method_id == t2.a_id WHERE t2.a_access_method_name == ? AND t1.t_access_method_value == ?;");
			stmt.setString(1, accessMethodName);
			stmt.setString(2, accessMethodValue);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
                info.add(rs.getInt("t_id"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return info;
	}

    public static void addHTMLElement(String pageName, String elementId, String value, String xpath, String type, String name, String className, String tag, String cssSelector, int versionId) {
        addPage(pageName,versionId);
        int pageNameId = getPageId(pageName,versionId);
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO HtmlElements (h_page_name_id, h_element_id, h_value, h_xpath, h_type, h_name, h_class_name, h_tag,  h_css_selector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, pageNameId);
            stmt.setString(2, elementId);
            stmt.setString(3, value);
            stmt.setString(4, xpath);
            stmt.setString(5, type);
            stmt.setString(6, name);
            stmt.setString(7, className);
            stmt.setString(8, tag);
            stmt.setString(9, cssSelector);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static int getMaxVid() {
        int vid = 0;
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(v_id) FROM Version;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vid = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vid;
    }

    public static int getPreviousVid() {
        int vid = 0;
        int maxVid = getMaxVid();
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(v_id) FROM Version WHERE v_id < ?;");
            stmt.setInt(1,maxVid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vid = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vid;
    }
    public static void addVersion(Timestamp time) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO Version (v_time) VALUES (?);");
            stmt.setTimestamp(1, time);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addPage(String pageName,int versionId) {

        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO Page (p_page_name,p_version_id) VALUES (?, ?);");
            stmt.setString(1, pageName);
            stmt.setInt(2, versionId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	public static void addTestElementPageRelation(int pageId,int testId,int position) {

        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO TestElementPageRelation (pr_java_test_elements_id,pr_page_id,pr_positon) VALUES (?, ?,?);");
            stmt.setInt(1, testId);
            stmt.setInt(2, pageId);
            stmt.setInt(3, position);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //
//public static int getIdOfHtmlElementByTestElement(TestElement testElement, int versionId) {
//    int result = 0;
//    try {
//        String accessMethodName = testElement.getAccessMethod();
//        PreparedStatement stmt;
//        switch (accessMethodName){
//            case "tag":
//                stmt = conn
//                        .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id == t3.v_id WHERE t3.v_id==? AND t1.h_tag == ?;");
//                break;
//            case "className":
//                stmt = conn
//                        .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id  LEFT OUTER JOIN Version t3 ON t2.p_version_id == t3.v_id WHERE t3.v_id==? AND t1.h_class_name == ?;");
//                break;
//            case "name":
//                stmt = conn
//                        .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id  LEFT OUTER JOIN Version t3 ON t2.p_version_id == t3.v_id WHERE t3.v_id==? AND t1.h_name == ?;");
//                break;
//            default:
//                stmt = conn
//                        .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id  LEFT OUTER JOIN Version t3 ON t2.p_version_id == t3.v_id WHERE t3.v_id==? AND t1.h_xpath == ?;");
//                break;
//        }
//        stmt.setInt(1, versionId);
//        stmt.setString(2, testElement.getAccessMethodValue());
//        ResultSet rs = stmt.executeQuery();
//        while (rs.next()) {
//            result = rs.getInt("h_id");
//        }
//        rs.close();
//    } catch (SQLException ex) {
//        ex.printStackTrace();
//    }
//    return result;
//}
    public static int getIdOfHtmlElementByTestElement(TestElement testElement, int versionId1, int versionId2, boolean notExist, boolean getFirst, int pageId) {
        int result = 0;
        try {
            String accessMethodName = testElement.getAccessMethod();
            PreparedStatement stmt;
            switch (accessMethodName){
                case "tag":
                    stmt = conn
                            .prepareStatement(" SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id  where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ? and h_tag == ? and p_id == ?;");
                    break;
                case "className":
                    stmt = conn
                            .prepareStatement(" SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id  where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ? and h_class_name == ? and p_id == ?;");
                    break;
                case "name":
                    stmt = conn
                            .prepareStatement(" SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id  where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ? and h_name == ? and p_id == ?;");
                    break;
                default:
                    stmt = conn
                            .prepareStatement(" SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id  where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ? and h_xpath == ? and p_id == ?;");
                    break;
            }
            stmt.setInt(1, versionId1);
            stmt.setInt(2, versionId2);
            if(notExist){
                stmt.setInt(3, 1);
            }
            else{
                stmt.setInt(3, 2);
            }
            if(getFirst){
                stmt.setInt(4, versionId1);
            }
            else{
                stmt.setInt(4, versionId2);
            }
            stmt.setString(5, testElement.getAccessMethodValue());

			stmt.setInt(6, pageId);

			ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("h_id");
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }


    public static int getPageId(String pageName, int versionId) {
        int result = 0;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT p_id FROM Page WHERE p_page_name = ? and p_version_id = ?;");
            stmt.setString(1, pageName);
            stmt.setInt(2, versionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getPageNames(int versionId) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT p_page_name FROM Page  JOIN Version V on Page.p_version_id = V.v_id WHERE v_id = ?;");
            stmt.setInt(1, versionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getIdOfHtmlElement(HTMLElement htmlElement) {
        int result = 0;
        try {
            /*PreparedStatement stmt = conn
                    .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id  WHERE t2.p_page_name == ? AND t1.h_element_id == ? AND t1.h_value == ? AND t1.h_xpath == ? AND t1.h_type == ? AND t1.h_name == ? AND t1.h_class_name == ? AND t1.h_tag == ? AND  t1.h_css_selector == ?;");
            stmt.setString(1, htmlElement.getUrl());
            stmt.setString(2, htmlElement.getId());
            stmt.setString(3, htmlElement.getValue());
            stmt.setString(4, htmlElement.getXpath());
            stmt.setString(5, "TypeNotDone");
            stmt.setString(6, htmlElement.getName());
            stmt.setString(7, htmlElement.getClassName());
            stmt.setString(8, htmlElement.getTag());*/
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT h_id FROM HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id  WHERE t1.h_xpath == ?;");
            stmt.setString(1, htmlElement.getXpath());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("h_id");
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Integer> getIdOfDifferentHtmlElements(int versionId1, int versionId2, boolean notExist, boolean getFirst) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id  where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ?;");
            stmt.setInt(1, versionId1);
            stmt.setInt(2, versionId2);
            if(notExist){
                stmt.setInt(3, 1);
            }
            else{
                stmt.setInt(3, 2);
            }
            if(getFirst){
                stmt.setInt(4, versionId1);
            }
            else{
                stmt.setInt(4, versionId2);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add( rs.getInt(1));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static ArrayList<TestElement> getTestElementWhichTestAffectedByDiffHtmlElements(int versionId1, int versionId2, boolean notExist, boolean getFirst, String pageName) {
        ArrayList<TestElement> result = new ArrayList<TestElement>();
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT * FROM HtmlElements JOIN Page On p_id == h_page_name_id JOIN Version On p_version_id = v_id JOIN ElementDependencies ED on HtmlElements.h_id = ED.d_html_elements_id JOIN  TestElementPageRelation TEPR ON ED.d_position == TEPR.pr_positon and ED.d_java_test_elements_id = TEPR.pr_java_test_elements_id AND p_id == TEPR.pr_page_id JOIN JavaTestElements t1 ON TEPR.pr_java_test_elements_id == t1.t_id LEFT OUTER JOIN AccessMethods t2 ON t1.t_access_method_id == t2.a_id LEFT OUTER JOIN ActionMethods t3 ON t1.t_action_method_id == t3.ac_id where h_id IN (SELECT Table1.h_id FROM (HtmlElements t1 LEFT OUTER JOIN Page t2 ON t1.h_page_name_id == t2.p_id LEFT OUTER JOIN Version t3 ON t2.p_version_id== t3.v_id) Table1 WHERE Table1.v_id == ? OR Table1.v_id == ? GROUP BY Table1.p_page_name, Table1.h_element_id, Table1.h_value, Table1.h_xpath, Table1.h_type, Table1.h_name, Table1.h_class_name, Table1.h_css_selector, Table1.h_tag HAVING COUNT(*) = ?) and p_version_id == ? and p_page_name=?;");
            stmt.setInt(1, versionId1);
            stmt.setInt(2, versionId2);
            if(notExist){
                stmt.setInt(3, 1);
            }
            else{
                stmt.setInt(3, 2);
            }
            if(getFirst){
                stmt.setInt(4, versionId1);
            }
            else{
                stmt.setInt(4, versionId2);
            }
            stmt.setString(5, pageName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TestElement testElement = new TestElement();
                testElement.setAccessMethod(rs.getString("a_access_method_name"));
                testElement.setAccessMethodValue(rs.getString("t_access_method_value"));
                testElement.setActionMethod(rs.getString("ac_action_method_name"));
                testElement.setActionMethodValue(rs.getString("t_action_method_value"));
                Position p = new Position();
                p.setLine( rs.getInt("d_position"));
                testElement.setStartingPosition(p);
                testElement.setTestName(rs.getString("d_test_name"));
                testElement.setTestClassName(rs.getString("d_class_name"));
                testElement.setUrl(rs.getString("p_page_name"));

                result.add(testElement);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addTestElement(String accessMethod, String accessMethodValue, String actionMethod, String actionMethodValue) {
        addAccessMethod(accessMethod);
        addActionMethod(actionMethod);
        int aId = getAccessMethodId(accessMethod);
        int acId = getActionMethodId(actionMethod);
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO JavaTestElements (t_access_method_id, t_access_method_value, t_action_method_id, t_action_method_value) VALUES (?, ?, ?, ?);");
            stmt.setInt(1, aId);
            stmt.setString(2, accessMethodValue);
            stmt.setInt(3, acId);
            stmt.setString(4, actionMethodValue);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void addAccessMethod(String accessMethod) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO AccessMethods (a_access_method_name) VALUES (?);");
            stmt.setString(1, accessMethod);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int getAccessMethodId(String accessMethod) {
        int result = 0;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT a_id FROM AccessMethods WHERE a_access_method_name = ?;");
            stmt.setString(1, accessMethod);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addActionMethod(String actionMethod) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO ActionMethods (ac_action_method_name) VALUES (?);");
            stmt.setString(1, actionMethod);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int getActionMethodId(String actionMethod) {
        int result = 0;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT ac_id FROM ActionMethods WHERE ac_action_method_name = ?;");
            stmt.setString(1, actionMethod);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public static int getDependenciesIdFromTestMethod(int testId, String className, String testName, int position) {
        int result = 0;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT d_id FROM JavaTestElements JOIN ElementDependencies ED on JavaTestElements.t_id == d_java_test_elements_id WHERE t_id == ? and d_class_name == ? and d_test_name == ? and d_position == ?;");
            stmt.setInt(1, testId);
            stmt.setString(2, className);
            stmt.setString(3, testName);
            stmt.setInt(4, position);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addElementDependencies(int javaTestElementsId, int htmlElementsId, String className, String testName, int position) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO ElementDependencies (d_java_test_elements_id, d_html_elements_id, d_class_name, d_test_name, d_position) VALUES (?, ?, ?, ?, ?);");
            stmt.setInt(1, javaTestElementsId);
            stmt.setInt(2, htmlElementsId);
            stmt.setString(3, className);
            stmt.setString(4, testName);
            stmt.setInt(5, position);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static ArrayList<TestElement> listAllTestElements() {
        ArrayList<TestElement> info = new ArrayList<TestElement>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JavaTestElements t1 LEFT OUTER JOIN AccessMethods t2 ON t1.t_access_method_id == t2.a_id LEFT OUTER JOIN ActionMethods t3 ON t1.t_action_method_id == t3.ac_id;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TestElement testElement = new TestElement();
                testElement.setAccessMethod(rs.getString("a_access_method_name"));
                testElement.setAccessMethodValue(rs.getString("t_access_method_value"));
                testElement.setActionMethod(rs.getString("ac_action_method_name"));
                testElement.setActionMethodValue(rs.getString("t_action_method_value"));
                info.add(testElement);
                //System.out.println("ID of TestElement: "+rs.getInt("t_id"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return info;
    }

    public static void addDummy(String text,String page) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT OR IGNORE INTO DummyTable (text,page) VALUES (?, ?);");
            stmt.setString(1, text);
            stmt.setString(2, page);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(){
        try{
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());

        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

                System.out.println(ex.getMessage());
            }
        }
    }
}
