package com.github.chen0040.si.dsl;


/**
 * Created by xschen on 8/5/2017.
 */
public class VariablePair {
   private Variable variableOne;
   private Variable variableTwo;


   public VariablePair(Variable variableOne, Variable variableTwo) {
      this.variableOne = variableOne;
      this.variableTwo = variableTwo;
   }

   public PairedSampleKie numericalSample() {
      return new PairedSampleKie(this);
   }

   public Variable variable1(){
      return variableOne;
   }

   public Variable variable2() {
      return variableTwo;
   }
}
