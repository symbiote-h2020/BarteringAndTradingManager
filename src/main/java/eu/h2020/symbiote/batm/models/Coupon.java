package eu.h2020.symbiote.batm.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.json.JsonParser;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Coupon model
 *
 * @author jamsellem
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon {
    /**
     * coupon identifier
     */
    @Id
    private String id;

    /**
     * user history
     */
    @NotNull
    private List<String> usedBy;

    /**
     * (platformId): Who issued the coupon.
     */
    @NotBlank
    private String issuer;

    /**
     * (platformId) (optional): Who is the beneficiary of the coupon. This is an optional field and, if left as such, it can be passed around through several platforms
     */
    private String beneficiary;

    /**
     * (federationId): The federation this coupon belongs to
     */
    @NotBlank
    private String fedIdentifier;

    /**
     * Type of resources being bartered
     */
    @NotBlank
    private String resourceType;

    /**
     * Expiry date of the coupon
     */
    @NotNull
    private Date expirationDate;

    /**
     * Boolean indicating if the coupon can be used only once, or several times
     */
    @NotNull
    private Boolean singleUse;


    public Coupon(){}

    public Coupon(Coupon c) {
        this.id = c.id;
        this.usedBy = c.usedBy;
        this.issuer = c.issuer;
        this.beneficiary = c.beneficiary;
        this.fedIdentifier = c.fedIdentifier;
        this.resourceType = c.resourceType;
        this.expirationDate = c.expirationDate;
        this.singleUse = c.singleUse;
    }

    public String getId(){ return id;}

    public String getIssuer() { return issuer; }

    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getBeneficiary() { return beneficiary; }

    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }

    public String getFedIdentifier() { return fedIdentifier; }

    public void setFedIdentifier(String fedIdentifier) { this.fedIdentifier = fedIdentifier; }

    public String getResourceType() { return resourceType; }

    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public Date getExpirationDate() { return expirationDate; }

    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public boolean isSingleUse() { return singleUse; }

    public void setSingleUse(boolean singleUse) { this.singleUse = singleUse; }

    public List<String> getUsedByList() {
        if(usedBy == null){
            usedBy = new ArrayList<>();
        }
        return usedBy;
    }

    public void addToUsedByList(String usedBy) {
        if(this.usedBy == null){
            this.usedBy = new ArrayList<>();
        }
        this.usedBy.add(usedBy);
    }
}


