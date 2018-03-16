package org.takeback.core.user;

public class UserFundStatistics
{
  private Double accumulatedIncome = Double.valueOf(0.0D);
  

  private Double expectIncome = Double.valueOf(0.0D);
  

  private Double recyclePrincipal = Double.valueOf(0.0D);
  

  private Double totalFinanAmount = Double.valueOf(0.0D);
  

  private Double repayAmount = Double.valueOf(0.0D);
  

  private Double alreadyRepayAmount = Double.valueOf(0.0D);
  

  private Double totalInvest = Double.valueOf(0.0D);
  

  private Integer countInvest = Integer.valueOf(0);
  

  private Integer countFinanAmount = Integer.valueOf(0);
  
  public Double getAccumulatedIncome() {
    return this.accumulatedIncome;
  }
  
  public void setAccumulatedIncome(Double accumulatedIncome) {
    this.accumulatedIncome = accumulatedIncome;
  }
  
  public void addAccumulatedIncome(Double accumulatedIncome) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.accumulatedIncome = Double.valueOf(localUserFundStatistics.accumulatedIncome.doubleValue() + accumulatedIncome.doubleValue()));
  }
  
  public Double getExpectIncome() {
    return this.expectIncome;
  }
  
  public void setExpectIncome(Double expectIncome) {
    this.expectIncome = expectIncome;
  }
  
  public void addExpectIncome(Double expectIncome)
  {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.expectIncome = Double.valueOf(localUserFundStatistics.expectIncome.doubleValue() + expectIncome.doubleValue()));
  }
  
  public Double getRecyclePrincipal() {
    return this.recyclePrincipal;
  }
  
  public void setRecyclePrincipal(Double recyclePrincipal) {
    this.recyclePrincipal = recyclePrincipal;
  }
  
  public void addRecyclePrincipal(Double recyclePrincipal) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.recyclePrincipal = Double.valueOf(localUserFundStatistics.recyclePrincipal.doubleValue() + recyclePrincipal.doubleValue()));
  }
  
  public Double getTotalFinanAmount() {
    return this.totalFinanAmount;
  }
  
  public void setTotalFinanAmount(Double totalFinanAmount) {
    this.totalFinanAmount = totalFinanAmount;
  }
  
  public void addTotalFinanAmount(Double totalFinanAmount) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.totalFinanAmount = Double.valueOf(localUserFundStatistics.totalFinanAmount.doubleValue() + totalFinanAmount.doubleValue()));
  }
  
  public Double getRepayAmount() {
    return this.repayAmount;
  }
  
  public void setRepayAmount(Double repayAmount) {
    this.repayAmount = repayAmount;
  }
  
  public void addRepayAmount(Double repayAmount) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.repayAmount = Double.valueOf(localUserFundStatistics.repayAmount.doubleValue() + repayAmount.doubleValue()));
  }
  
  public Double getAlreadyRepayAmount() {
    return this.alreadyRepayAmount;
  }
  
  public void setAlreadyRepayAmount(Double alreadyRepayAmount) {
    this.alreadyRepayAmount = alreadyRepayAmount;
  }
  
  public void addAlreadyRepayAmount(Double alreadyRepayAmount) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.alreadyRepayAmount = Double.valueOf(localUserFundStatistics.alreadyRepayAmount.doubleValue() + alreadyRepayAmount.doubleValue()));
  }
  
  public void setCountInvest(Integer countInvest)
  {
    this.countInvest = countInvest;
  }
  
  public void setTotalInvest(Double totalInvest) {
    this.totalInvest = totalInvest;
  }
  
  public void setCountFinanAmount(Integer countFinanAmount) {
    this.countFinanAmount = countFinanAmount;
  }
  
  public Double getTotalInvest() {
    return this.totalInvest;
  }
  
  public void addTotalInvest(Double totalInvest)
  {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.totalInvest = Double.valueOf(localUserFundStatistics.totalInvest.doubleValue() + totalInvest.doubleValue()));
  }
  
  public Integer getCountInvest() {
    return this.countInvest;
  }
  
  public void addCountInvest(Integer countInvest) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.countInvest = Integer.valueOf(localUserFundStatistics.countInvest.intValue() + countInvest.intValue()));
  }
  
  public Integer getCountFinanAmount() {
    return this.countFinanAmount;
  }
  
  public void addCountFinanAmount(Integer countFinanAmount) {
    UserFundStatistics localUserFundStatistics = this;(localUserFundStatistics.countFinanAmount = Integer.valueOf(localUserFundStatistics.countFinanAmount.intValue() + countFinanAmount.intValue()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\UserFundStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */