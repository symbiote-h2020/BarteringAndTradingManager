package eu.h2020.symbiote.batm.repositories.impl;

import eu.h2020.symbiote.batm.models.Coupon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.Date;
import java.util.List;

/**
 * Coupon custom repository implementation
 *
 * @author jamsellem
 */
public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private static Log logger = LogFactory.getLog(CouponRepositoryImpl.class);


    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Coupon> findValidCouponByResourceTypeAndFedId(String resourceType, String fedIdentifier, int limit){

/*
        (singleUse = false || (singleUse == true  && usedBy.size == 0))
        &&
        expirationDate.after( today )
        &&
        resourceType like arg resourceType
        &&
        fedIdentifier like arg fedIdentifier
*/

        Criteria criteria = new Criteria();
        criteria.andOperator(
                new Criteria().orOperator(
                        new Criteria().andOperator(Criteria.where("singleUse").is(true),Criteria.where("usedBy").size(0)),
                        Criteria.where("singleUse").is(false)
                ),
                Criteria.where("expirationDate").ne(null),
                Criteria.where("expirationDate").gt(new Date()),
                Criteria.where("resourceType").is(resourceType),
                Criteria.where("fedIdentifier").is(fedIdentifier)
        );

        Query query = new Query(criteria);

        if (limit > 0)
            query.limit(limit);

        return  mongoTemplate.find(query, Coupon.class);
    }
}
