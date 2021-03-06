//***************************************************************************
// (c) Copyright IBM Corp. 2007 All rights reserved.
// 
// The following sample of source code ("Sample") is owned by International 
// Business Machines Corporation or one of its subsidiaries ("IBM") and is 
// copyrighted and licensed, not sold. You may use, copy, modify, and 
// distribute the Sample in any form without payment to IBM, for the purpose of 
// assisting you in the development of your applications.
// 
// The Sample code is provided to you on an "AS IS" basis, without warranty of 
// any kind. IBM HEREBY EXPRESSLY DISCLAIMS ALL WARRANTIES, EITHER EXPRESS OR 
// IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Some jurisdictions do 
// not allow for the exclusion or limitation of implied warranties, so the above 
// limitations or exclusions may not apply to you. IBM shall not be liable for 
// any damages you suffer as a result of using, copying, modifying or 
// distributing the Sample, even if IBM has been advised of the possibility of 
// such damages.
//***************************************************************************
//
// SOURCE FILE NAME: TbIdent.java
//
// SAMPLE: How to use Identity Columns
//
// SQL Statements USED:
//         CREATE TABLE
//         INSERT
//         SELECT
//         DROP
//
// Classes used from Util.java are:
//         Db
//         Data
//         JdbcException
//
// Compile: the utility file and the source file with:
//		javac Util.java
//		javac <filename>.java
//
// Run: java <filename> [<db_name>] <username> <pwd>
//	or
//     java <filename> [<db_name>] <server_name> <port_num> <username> <pwd>
//
// OUTPUT FILE: TbIdent.out (available in the online documentation)
// Output will vary depending on the JDBC driver connectivity used.
//***************************************************************************
//
// For more information on the sample programs, see the README file.
//
// For information on developing JDBC applications, see the Application
// Development Guide.
//
// For information on using SQL statements, see the SQL Reference.
//
// For the latest information on programming, compiling, and running DB2
// applications, visit the DB2 application development website at
//     http://www.software.ibm.com/data/db2/udb/ad
//**************************************************************************/

import java.sql.*;
import java.lang.*;

public class TbIdent
{
  public static void main(String argv[])
  {
    try
    {
      Db db = new Db(argv);

      System.out.println();
      System.out.println("THIS SAMPLE SHOWS HOW TO USE IDENTITY COLUMNS");

      // connect to the 'sample' database
      db.connect();

      generateAlways(db.con);
      generateByDefault(db.con);

      // disconnect from the 'sample' database
      db.disconnect();
    }
    catch (Exception e)
    {
      JdbcException jdbcExc = new JdbcException(e);
      jdbcExc.handle();
    }
  } // main

  static void generateAlways(Connection con)
  {
    System.out.println();
    System.out.println(
      "----------------------------------------------------------\n" +
      "USE THE SQL STATEMENT:\n" +
      "  CREATE TABLE\n" +
      "  INSERT\n" +
      "TO CREATE AN IDENTITY COLUMN WITH VALUE 'GENERATED ALWAYS'\n" +
      "AND TO INSERT DATA IN THE TABLE\n");

    try
    {
      // Create the table 'building'
      System.out.println(
        "  CREATE TABLE building(\n" +
        "    bldnum INT GENERATED ALWAYS AS IDENTITY\n" +
        "      (START WITH 1, INCREMENT BY 1),\n" +
        "    addr VARCHAR(20),\n" +
        "    city VARCHAR(10),\n" +
        "    floor SMALLINT,\n" +
        "    employees SMALLINT)\n");

      Statement stmt = con.createStatement();

      stmt.executeUpdate(
        "CREATE TABLE building(bldnum INT GENERATED ALWAYS" +
        " AS IDENTITY(START WITH 1, INCREMENT BY 1), " +
        "                      addr VARCHAR(20), " +
        "                      city VARCHAR(10), " +
        "                      floors SMALLINT, " +
        "                      employees SMALLINT) " );
      stmt.close();

      // Insert data into the table 'building'
      System.out.println(
        "  INSERT INTO building(bldnum, addr, city, floors, employees)\n" +
        "    VALUES(DEFAULT, '110 Woodpart St', 'Smithville', 3, 10),\n" +
        "          (DEFAULT, '123 Sesame Ave', 'Jonestown', 16, 13),\n" +
        "          (DEFAULT, '738 Eglinton Rd', 'Whosburg', 2, 10),\n" +
        "          (DEFAULT, '832 Lesley Blvd', 'Centertown', 2, 18)\n");

      Statement stmt1 = con.createStatement();
      stmt1.executeUpdate(
        "INSERT INTO building(bldnum, addr, city, floors, employees)" +
        " VALUES(DEFAULT, '110 Woodpart St', 'Smithville', 3, 10), " +
        "(DEFAULT, '123 Sesame Ave', 'Jonestown', 16, 13), " +
        "(DEFAULT, '738 Eglinton Rd', 'Whosburg', 2, 10), " +
        "(DEFAULT, '832 Lesley Blvd', 'Centertown', 2, 18)");

      stmt1.close();

      // Retrieve and display the content of the 'building' table
      System.out.println(
        "  SELECT * FROM building\n" +
        "    ID  ADDRESS              CITY         FLOORS EMP\n" +
        "    --- -------------------- ------------ ------ ---\n");
      Statement stmt3 = con.createStatement();
      ResultSet rs = stmt3.executeQuery("SELECT * FROM building");

      while (rs.next())
      {
        System.out.println("    " +
        Data.format(rs.getString("bldnum"),3) + " " +
        Data.format(rs.getString("addr"),20) + " " +
        Data.format(rs.getString("city"),12) + " " +
        Data.format(rs.getString("floors"),6) + " " +
        Data.format(rs.getString("employees"),4));
      }

      rs.close();
      stmt3.close();

      // Drop the table 'building'
      System.out.println();
      System.out.println("  Dropping the table 'building'\n");
      Statement stmt4 = con.createStatement();
      stmt4.executeUpdate("DROP TABLE building");
      stmt4.close();

      con.commit();
    }
    catch (Exception e)
    {
      JdbcException jdbcExc = new JdbcException(e);
      jdbcExc.handle();
    }
  }  // generatedAlways

  static void generateByDefault(Connection con)
  {
    System.out.println();
    System.out.println(
      "----------------------------------------------------------\n" +
      "USE THE SQL STATEMENT:\n" +
      "  CREATE TABLE\n" +
      "  INSERT\n" +
      "TO CREATE AN IDENTITY COLUMN WITH VALUE 'GENERATED BY DEFAULT'\n" +
      "AND TO INSERT DATA IN THE TABLE\n");

    try
    {
      // Create the table 'warehouse'
      System.out.println(
        "  CREATE TABLE warehouse(\n" +
        "    whnum INT GENERATED BY DEFAULT AS IDENTITY\n" +
        "      (START WITH1, INCREMENT BY 1),\n" +
        "    addr VARCHAR(20),\n" +
        "    city VARCHAR(10),\n" +
        "    capacity SMALLINT,\n" +
        "    employees SMALLINT)\n");

      Statement stmt = con.createStatement();
      stmt.executeUpdate(
        "CREATE TABLE warehouse(whnum INT GENERATED BY DEFAULT" +
        " AS IDENTITY(START WITH 1, INCREMENT BY 1), " +
        "                       addr VARCHAR(20), " +
        "                       city VARCHAR(10), " +
        "                       capacity SMALLINT, " +
        "                       employees SMALLINT) ");
      stmt.close();

      // Insert data into the table 'warehouse'
      System.out.println(
        "  INSERT INTO warehouse(whnum, addr, city, capacity, employees)\n" +
        "    VALUES(DEFAULT, '92 Bothfield Dr', 'Yorkvile', 23, 100),\n" +
        "          (DEFAULT, '33 Giant Road', 'Centertown', 100, 22),\n" +
        "          (3, '8200 Warden Blvd', 'Smithville', 254, 10),\n" +
        "          (DEFAULT, '53 4th Ave', 'Whosburg', 97, 28)\n");

      Statement stmt1 = con.createStatement();
      stmt1.executeUpdate(
        "INSERT INTO warehouse(whnum, addr, city, capacity, employees)" +
        " VALUES(DEFAULT, '92 Bothfield Dr', 'Yorkvile', 23, 100), " +
        "(DEFAULT, '33 Giant Road', 'Centertown', 100, 22), " +
        "(3, '8200 Warden Blvd', 'Smithville', 254, 10), " +
        "(DEFAULT, '53 4th Ave', 'Whosburg', 97, 28) ");

      stmt1.close();

      //Print warhouse Table
      System.out.println(
        "  SELECT * FROM warehouse\n" +
        "    ID  ADDRESS              CITY         CAPACITY EMP\n" +
        "    --- -------------------- ------------ -------- ---\n");

      Statement stmt3 = con.createStatement();
      ResultSet rs = stmt3.executeQuery("SELECT * FROM warehouse");

      while (rs.next())
      {
        System.out.println("    " +
        Data.format(rs.getString("whnum"),3) + " " +
        Data.format(rs.getString("addr"),20) + " " +
        Data.format(rs.getString("city"),12) + " " +
        Data.format(rs.getString("capacity"),8) + " " +
        Data.format(rs.getString("employees"),4));
      }

      rs.close();
      stmt3.close();

      System.out.println(
        "\n  NOTE:\n" +
        "  An Identity Column with value 'GENERATED BY DEFAULT' may\n" +
        "  not contain a unique value for each row! To ensure a unique\n" +
        "  value for each row, define an index on the Identity Column.\n");

      //Drop warhouse Table
      System.out.println("  Dropping the table 'warehouse'");
      Statement stmt4 = con.createStatement();
      stmt4.executeUpdate("DROP TABLE warehouse");
      stmt4.close();

      con.commit();
    }
    catch (Exception e)
    {
      JdbcException jdbcExc = new JdbcException(e);
      jdbcExc.handle();
    }
  }  //generatedByDefault
}
