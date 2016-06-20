package com.gorilla.dao.impl;

import com.gorilla.dao.PriceModifierDao;
import com.gorilla.domain.PriceModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class PriceModifierDaoJdbcImpl implements PriceModifierDao {

    @Autowired
    @Qualifier("h2DataSource")
    private DataSource dataSource;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final static String GET_BY_ID_SQL = "select * from INSTRUMENT_PRICE_MODIFIER mod where mod.name = ? ";

    @Override
    @Cacheable(value = "instrumentModifierCache", key = "#instrumentName")
    public Optional<PriceModifier> getByInstrumentName(String instrumentName) {

        PriceModifier result = null;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(GET_BY_ID_SQL);
            ps.setString(1, instrumentName);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String multiplier = rs.getString("multiplier");

                result = new PriceModifier(id, name, multiplier);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            LOG.error("Cannot get entry '" + instrumentName + "' from db, reason: " + e);
            return Optional.empty();
        } finally {
            try{
                if(ps!=null) ps.close();
            } catch(SQLException ignore){}
            try{
                if(conn!=null) conn.close();
            }catch(SQLException ignore){}
        }

        return Optional.ofNullable(result);
    }

}
