
package com.zibilal.layouttestapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BadgeCounts {

    @SerializedName("bronze")
    @Expose
    private Integer bronze;
    @SerializedName("silver")
    @Expose
    private Integer silver;
    @SerializedName("gold")
    @Expose
    private Integer gold;

    /**
     * 
     * @return
     *     The bronze
     */
    public Integer getBronze() {
        return bronze;
    }

    /**
     * 
     * @param bronze
     *     The bronze
     */
    public void setBronze(Integer bronze) {
        this.bronze = bronze;
    }

    /**
     * 
     * @return
     *     The silver
     */
    public Integer getSilver() {
        return silver;
    }

    /**
     * 
     * @param silver
     *     The silver
     */
    public void setSilver(Integer silver) {
        this.silver = silver;
    }

    /**
     * 
     * @return
     *     The gold
     */
    public Integer getGold() {
        return gold;
    }

    /**
     * 
     * @param gold
     *     The gold
     */
    public void setGold(Integer gold) {
        this.gold = gold;
    }

}
