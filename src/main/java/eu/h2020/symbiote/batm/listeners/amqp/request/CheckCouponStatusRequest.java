package eu.h2020.symbiote.batm.listeners.amqp.request;

// TODO ~ add this to security.communication.payloads

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.security.communication.payloads.Credentials;

public class CheckCouponStatusRequest {

    private final Credentials adminCredentials;
    private final Coupon coupon;
    private final CouponType type;
    private final String platformURL;

    public enum CouponType { CREATED, RESEND }


    public CheckCouponStatusRequest(@JsonProperty("adminCredentials") Credentials adminCredentials,
                                    @JsonProperty("coupon") Coupon coupon,
                                    @JsonProperty("type") CouponType type,
                                    @JsonProperty("platformUrl") String platformURL) {
        this.adminCredentials = adminCredentials;
        this.coupon = coupon;
        this.type = type;
        this.platformURL = platformURL;
    }


    public Credentials getAdminCredentials() { return adminCredentials; }

    public Coupon getCoupon() { return coupon; }

    public CouponType getType() { return type; }

    public String getPlatformURL() { return platformURL; }
}
