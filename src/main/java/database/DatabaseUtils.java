package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DatabaseUtils {
    private DatabaseUtils() {}

    static List<String> getTables(Connection connection, String databaseName) {
        String queryString = "Select DISTINCT(TABLES.TABLE_NAME) from information_schema.TABLES where TABLES.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> searchForTablesLike(Connection connection, String databaseName, String searchString) {
        String queryString = "SELECT DISTINCT(TABLES.TABLE_NAME) FROM information_schema.TABLES WHERE TABLES.TABLE_NAME LIKE ? AND TABLES.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, searchString, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> searchForColumns(Connection connection, String databaseName, String searchString) {
        String queryString = "SELECT DISTINCT(COLUMNS.COLUMN_NAME) FROM information_schema.COLUMNS WHERE COLUMNS.COLUMN_NAME LIKE ? AND COLUMNS.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, searchString, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> getColumns(Connection connection, String databaseName, String tableName) {
        String queryString = "SELECT DISTINCT(COLUMNS.COLUMN_NAME) FROM information_schema.COLUMNS WHERE COLUMNS.TABLE_NAME = ? AND COLUMNS.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, tableName, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> getColumnsLike(Connection connection, String databaseName, String columnquery, String tableName) {
        String queryString = "SELECT DISTINCT(COLUMNS.COLUMN_NAME) FROM information_schema.COLUMNS WHERE COLUMNS.TABLE_NAME = ?  AND COLUMN_NAME LIKE ? AND COLUMNS.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, tableName, columnquery, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> getColumn(Connection connection, String databaseName, String columnName, String tableName) {
        String queryString = "SELECT DISTINCT(COLUMNS.COLUMN_NAME) FROM information_schema.COLUMNS WHERE COLUMNS.COLUMN_NAME = ? AND COLUMNS.TABLE_NAME = ? AND COLUMNS.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, columnName, tableName, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> getTablesFromColumnLike(Connection connection, String databaseName, String searchString) {
        String queryString = "SELECT DISTINCT(COLUMNS.TABLE_NAME) FROM information_schema.COLUMNS WHERE COLUMNS.COLUMN_NAME LIKE ? AND COLUMNS.TABLE_SCHEMA = ?;";
        try {
            return genResultsList(connection, queryString, searchString, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> genResultsList(Connection connection, String queryString, String setString) throws SQLException {
        List<String> output = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, setString);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            output.add(resultSet.getString(1));
        }
        return output;
    }

    private static List<String> genResultsList(Connection connection, String queryString, String setString, String database) throws SQLException {
        List<String> output = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, setString);
        preparedStatement.setString(2, database);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            output.add(resultSet.getString(1));
        }
        return output;
    }

    private static List<String> genResultsList(Connection connection, String queryString, String setString1, String setString2, String database) throws SQLException {
        List<String> output = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, setString1);
        preparedStatement.setString(2, setString2);
        preparedStatement.setString(3, database);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            output.add(resultSet.getString(1));
        }
        return output;
    }

    static List<String> createQueries_objectInAnyTable(Connection connection, String database, String searchQuery) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' LIKE \\'" + searchQuery + "\\'') \n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'VARCHAR';";
        return genQueriesList(connection, genQueries);
    }

    static List<String> createQueries_objectInColumn(Connection connection, String database, String searchQuery, String columnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' LIKE \\'" + searchQuery + "\\'')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'VARCHAR' AND COLUMN_NAME = '" + columnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_objectInTable(Connection connection, String database, String searchQuery, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' LIKE \\'" + searchQuery + "\\'')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'VARCHAR' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_objectInTableInColumn(Connection connection, String database, String searchQuery, String tableName, String columnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' LIKE \\'" + searchQuery + "\\'')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'VARCHAR' AND TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + columnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectInAnyTable(Connection connection, String database, String searchQuery) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectInColumn(Connection connection, String database, String searchQuery, String columnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + columnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectInTable(Connection connection, String database, String searchQuery, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectInColumnInTable(Connection connection, String database, String searchQuery, String columnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + columnName + "' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenInAnyTable(Connection connection, String database, String searchQuery1, String searchQuery2) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenInColumn(Connection connection, String database, String searchQuery1, String searchQuery2, String columnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + columnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenInTable(Connection connection, String database, String searchQuery1, String searchQuery2, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenInColumnInTable(Connection connection, String database, String searchQuery1, String searchQuery2, String columnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT * FROM ', TABLE_SCHEMA, '.', TABLE_NAME, \n" +
                "              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS \n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + columnName + "' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectShowColumnInColumnInTable(Connection connection, String database, String searchQuery, String showColumnName, String inColumnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + inColumnName + "' AND TABLE_NAME = '"+ tableName +"';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectShowColumnInColumn(Connection connection, String database, String searchQuery, String showColumnName, String inColumnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + inColumnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectShowColumnInTable(Connection connection, String database, String searchQuery, String showColumnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectShowColumn(Connection connection, String database, String searchQuery, String showColumnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery + " ')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenShowColumnInColumnInTable(Connection connection, String database, String searchQuery1, String searchQuery2, String showColumnName, String inColumnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + inColumnName + "' AND TABLE_NAME = '"+ tableName +"';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenShowColumnInColumn(Connection connection, String database, String searchQuery1, String searchQuery2, String showColumnName, String inColumnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND COLUMN_NAME = '" + inColumnName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenShowColumnInTable(Connection connection, String database, String searchQuery1, String searchQuery2, String showColumnName, String tableName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT' AND TABLE_NAME = '" + tableName + "';";
        return genQueriesList(connection, genQueries);
    }

    static List <String> createQueries_numberObjectBetweenShowColumn(Connection connection, String database, String searchQuery1, String searchQuery2, String showColumnName) throws SQLException {
        String genQueries = "SELECT CONCAT('SELECT ', '" + showColumnName + "', ' FROM ', TABLE_SCHEMA, '.', TABLE_NAME,\n" +
                "                              ' WHERE ', COLUMN_NAME, ' " + searchQuery1 + " AND ', COLUMN_NAME, '" + searchQuery2 + "')\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_SCHEMA = '" + database + "' AND DATA_TYPE = 'INT';";
        return genQueriesList(connection, genQueries);
    }

    private static List<String> genQueriesList(Connection connection, String queryString) throws SQLException {
        System.out.println(queryString);
        PreparedStatement stmtCreateSearch = null;
        List<String> queries = new ArrayList<>();
        try {
            stmtCreateSearch = connection.prepareStatement(queryString);
            ResultSet resultSet = stmtCreateSearch.executeQuery();
            while (resultSet.next()) {
                queries.add(resultSet.getString(1));
            }
        } finally {
            if (stmtCreateSearch != null) { stmtCreateSearch.close(); }
        }
        return queries;
    }

    static List<Row> RunAllQueries(Connection connection, List<String> queries) {

        ArrayList<Row> results = new ArrayList<>();

        String unionQuery;

        Statement stmtUnionQuery;

        if (queries.size() < 1)
            return results;
        else if (queries.size() == 1)
            unionQuery = queries.get(0);
        else {
            Iterator<String> queryIterator = queries.iterator();
            StringBuilder unionQueryBuilder = new StringBuilder("(" + queryIterator.next() + ")\n");
            while (queryIterator.hasNext()) {
                unionQueryBuilder.append("UNION ALL\n").append("(").append(queryIterator.next()).append(")\n");
            }
            unionQuery = unionQueryBuilder.toString();
            System.out.println(unionQuery);
        }

        try {
            stmtUnionQuery = connection.createStatement();
            ResultSet resultSet = stmtUnionQuery.executeQuery(unionQuery);
            Row.formTable(resultSet, results);
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return results;
    }
}
