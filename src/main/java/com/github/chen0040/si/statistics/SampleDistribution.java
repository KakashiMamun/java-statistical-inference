package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.github.chen0040.data.exceptions.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * A sample distribution is an observed distribution of the values that a variable is observed to have for a sample of individuals. We have seen numerous sample distributions.
 */
@Getter
@Setter
public class SampleDistribution {

   private double sampleMean;
   private double sampleSd;
   private double sampleVariance;
   private boolean isNumeric;
   private int sampleSize;

   @Getter(AccessLevel.NONE)
   @Setter(AccessLevel.NONE)
   private double proportion;

   @Getter(AccessLevel.NONE)
   @Setter(AccessLevel.NONE)
   private String successLabel;

   @Setter(AccessLevel.NONE)
   private final String groupId;

   @Getter(AccessLevel.NONE)
   @Setter(AccessLevel.NONE)
   private double sumOfSquares;

   public SampleDistribution(Sample sample, String groupId){
      if(!sample.isNumeric()){
         throw new VariableWrongValueTypeException("The constructor can only work on numeric variables");
      }

      this.groupId = groupId;

      isNumeric = true;

      sampleMean = sample.getObservations().stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .map(Observation::getX)
              .reduce((a, b) -> a + b).get() / sample.countByGroupId(groupId);

      sumOfSquares = sample.getObservations().stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .map(o -> Math.pow(o.getX() - sampleMean, 2.0))
              .reduce((a, b) -> a + b).get();

      sampleVariance = sumOfSquares / (sample.countByGroupId(groupId)-1);

      sampleSd = Math.sqrt(sampleVariance);

      sampleSize = sample.countByGroupId(groupId);
   }

   public SampleDistribution(Sample sample, String successLabel, String groupId) {
      if(sample.isNumeric()) {
         throw new VariableWrongValueTypeException("The constructor can only work on categorical variables");
      }

      this.groupId = groupId;

      isNumeric = false;

      sampleMean = sample.countByGroupId(groupId) * sample.proportion(successLabel, groupId);

      this.proportion = sample.proportion(successLabel, groupId);
      sampleVariance =  sample.countByGroupId(groupId) * this.proportion * (1-this.proportion);

      sampleSd = Math.sqrt(sampleVariance);

      sampleSize = sample.countByGroupId(groupId);

      this.successLabel = successLabel;
   }


   public SampleDistribution(double sampleMean, int sampleSize, double sampleSd, String groupId) {
      this.sampleMean = sampleMean;
      this.sampleSize = sampleSize;
      this.sampleSd = sampleSd;
      this.sampleVariance = sampleSd * sampleSd;
      this.sumOfSquares = sampleVariance * (sampleSize - 1);
      this.isNumeric = true;
      this.groupId = groupId;
   }

   public SampleDistribution(String successLabel, double sampleProportion, int sampleSize, String groupId) {
      this.proportion = sampleMean;
      this.successLabel = successLabel;
      this.sampleSize = sampleSize;
      this.sampleVariance = sampleSize * ( 1 - proportion) * proportion;
      this.sampleMean = proportion * sampleSize;
      this.sampleSd = Math.sqrt(sampleVariance);
      this.sumOfSquares = sampleVariance * (sampleSize - 1);
      this.isNumeric = false;
      this.groupId = groupId;
   }


   public double getProportion(){
      if(isNumeric()){
         throw new NotImplementedException();
      }
      return proportion;
   }

   public void setProportion(double p) {
      proportion = p;
   }

   public String getSuccessLabel(){
      if(isNumeric()) {
         throw new NotImplementedException();
      }

      return successLabel;
   }

   public void setSuccessLabel(String successLabel) {
      this.successLabel = successLabel;
   }


   public boolean isCategorical() {
      return !isNumeric();
   }

   public double getSumOfSquares() {
      if(!isNumeric()){
         throw new NotImplementedException();
      }

      return sumOfSquares;
   }
}
