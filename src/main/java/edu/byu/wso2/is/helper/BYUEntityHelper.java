package edu.byu.wso2.is.helper;

/*
 *    Copyright 2016 Brigham Young University
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BYUEntityHelper {
    private static final Log log = LogFactory.getLog(BYUEntityHelper.class);
    
    private static DataSource ds = null;

    public BYUEntityHelper() {
        try {
            if (ds == null) {
                if (log.isDebugEnabled())
                    log.debug("CustomTokenGenerator: looking up  datasource");

                ds = (DataSource) new InitialContext().lookup("jdbc/BYUPRODB");

                if (log.isDebugEnabled())
                    log.debug("acquired datasource");
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public BYUEntity getBYUEntityFromNetId(String netid) {
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BYUEntity byuEntity = null;
        try {
            con = ds.getConnection();
            if (log.isDebugEnabled())
                log.debug("connection acquired. creating statement and executing query");
            statement = con.createStatement();
            resultSet = statement.executeQuery("select * from pro.person where net_id = '" + netid + "'");
            if (resultSet.next()) {
                String byu_id = resultSet.getString("byu_id");
                String person_id = resultSet.getString("person_id");
                String net_id = resultSet.getString("net_id");
                String surname = resultSet.getString("surname");
                String rest_of_name = resultSet.getString("rest_of_name");
                String surname_position = resultSet.getString("surname_position");
                String prefix = resultSet.getString("prefix");
                String suffix = resultSet.getString("suffix");
                String sort_name = resultSet.getString("sort_name");
                String preferred_firstname = resultSet.getString("preferred_first_name");
                if (log.isDebugEnabled())
                    log.debug("byu_id: " + byu_id + " person_id: " + person_id + " surname: " + surname + " rest_of_name: " + rest_of_name);
                byuEntity = new BYUEntity(net_id, person_id, byu_id, surname, rest_of_name, surname_position,prefix,suffix,sort_name,preferred_firstname);
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("resultset is empty");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
        return byuEntity;
    }

    public BYUEntity getBYUEntityFromPersonId(String personid) {
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BYUEntity byuEntity = null;
        try {
            con = ds.getConnection();
            if (log.isDebugEnabled())
                log.debug("connection acquired. creating statement and executing query");
            statement = con.createStatement();
            resultSet = statement.executeQuery("select * from pro.person where person_id = '" + personid + "'");
            if (resultSet.next()) {
                String byu_id = resultSet.getString("byu_id");
                String person_id = resultSet.getString("person_id");
                String net_id = resultSet.getString("net_id");
                String surname = resultSet.getString("surname");
                String rest_of_name = resultSet.getString("rest_of_name");
                String surname_position = resultSet.getString("surname_position");
                String prefix = resultSet.getString("prefix");
                String suffix = resultSet.getString("suffix");
                String sort_name = resultSet.getString("sort_name");
                String preferred_firstname = resultSet.getString("preferred_first_name");
                if (log.isDebugEnabled())
                    log.debug("byu_id: " + byu_id + " person_id: " + person_id + " surname: " + surname + " rest_of_name: " + rest_of_name);
                byuEntity = new BYUEntity(net_id, person_id, byu_id, surname, rest_of_name, surname_position,prefix,suffix,sort_name,preferred_firstname);
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("resultset is empty");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
        return byuEntity;
    }

    /* gets BYUEntity associated with a wso2 clientId (consumer_key) by checking the identity credential table for a mapping. */
    public BYUEntity getBYUEntityFromConsumerKey(String consumerKey) {
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BYUEntity byuEntity = null;
        try {
            con = ds.getConnection();
            if (log.isDebugEnabled())
                log.debug("connection acquired. creating statement and executing query");

            if (log.isDebugEnabled())
                log.debug("consumer key: " + consumerKey);

            statement = con.createStatement();
            String query = "select * from pro.person p, iam.credential c where p.byu_id = c.byu_id " +
                                "and c.credential_type = 'WSO2_CLIENT_ID' and c.credential_name = '" + consumerKey +"'";
            if (log.isDebugEnabled())
                log.debug("query: " + query);
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String byu_id = resultSet.getString("byu_id");
                String person_id = resultSet.getString("person_id");
                String net_id = resultSet.getString("net_id");
                String surname = resultSet.getString("surname");
                String rest_of_name = resultSet.getString("rest_of_name");
                String surname_position = resultSet.getString("surname_position");
                String prefix = resultSet.getString("prefix");
                String suffix = resultSet.getString("suffix");
                String sort_name = resultSet.getString("sort_name");
                String preferred_firstname = resultSet.getString("preferred_first_name");
                if (log.isDebugEnabled())
                    log.debug("byu_id: " + byu_id + " person_id: " + person_id + " surname: " + surname + " rest_of_name: " + rest_of_name);
                byuEntity = new BYUEntity(net_id, person_id, byu_id, surname, rest_of_name, surname_position,prefix,suffix,sort_name,preferred_firstname);
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("resultset is empty");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */ }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
        return byuEntity;
    }
}