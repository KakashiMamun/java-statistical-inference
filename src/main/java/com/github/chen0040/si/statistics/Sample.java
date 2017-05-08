package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableMixedValueTypeException;
import com.github.chen0040.si.exceptions.NoObservationFoundException;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by xschen on 3/5/2017.
 */
public class Sample {
   private final List<Observation> observations = new ArrayList<>();
   private Optional<Boolean> isNumeric = Optional.empty();
   private final Set<String> groups = new HashSet<>();
   private SampleMetaData metaData = new SampleMetaData();


   public void add(Observation observation) {
      if(isNumeric.isPresent()){
         boolean numericOnly = isNumeric.get();
         if(observation.isNumeric() != numericOnly) {
            throw new VariableMixedValueTypeException("sample should only contain ".concat(numericOnly ? "numeric" : "categorical").concat(" values"));
         }
      } else {
         isNumeric = Optional.of(observation.isNumeric());
      }

      groups.add(observation.getGroupId());

      observations.add(observation);
   }

   public boolean isNumeric(){
      if(!isNumeric.isPresent()){
         throw new NoObservationFoundException("No observation is found in the sample");
      }
      return isNumeric.get();
   }

   public boolean isCategorical() {
      return !isNumeric();
   }

   public int countByGroupId(String groupId) {
      return (int)observations.stream().filter(o -> groupId == null || groupId.equals(o.getGroupId())).count();
   }

   public Observation get(int index) {
      return observations.get(index);
   }


   public double proportion(String successLabel, String groupId) {
      if(isNumeric()) {
         throw new VariableWrongValueTypeException("proportional can only be calculated on categorical variables");
      }
      return (double)observations.stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .filter(o -> o.getCategoricalValue().equals(successLabel)).count() / countByGroupId(groupId);
   }


   public List<Observation> getObservations() {
      return observations;
   }

   public List<String> groups(){
      return groups.stream().collect(Collectors.toList());
   }


   public SampleMetaData metaData(){
      return metaData;
   }


   /**
    * return true if the sample contain two numeric variable x and y
    * @return
    */
   public boolean containsTwoNumericalVariables(){
      return observations.get(0).containsTwoNumericalVariables();
   }


}
