package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * ObstacleAttribute is used in the two-way binding between view and model.
 */
public class ObstacleAttribute<T> {

    /**
     * The Type Class type of the Attribute. This value is required in order to determine the
     * correct Input Form Type and Input Validation
     */
    public final Class<T> typeParameterClass;

    /**
     * The Value of the Attribute.
     * Example: T is Integer, then an example would be value=10
     */
    public T value;

    /**
     * Stores the name of the Attribute, and will be used to display the attribute label in the
     * form.
     */
    public String name;



    /**
     * typeParameterClass must be specified in order to use this ObstacleAttribute.
     * A default value is not suitable
     *
     * @param typeParameterClass
     */
    public ObstacleAttribute(Class<T> typeParameterClass, String name) {
        this.typeParameterClass = typeParameterClass;
        this.name = name;
    }

    public String getString(){
        if (typeParameterClass == Double.TYPE) {
            return Double.toString((Double) value);

        } else if (typeParameterClass == Integer.TYPE) {
            return Integer.toString((Integer) value);

        } else if (typeParameterClass == String.class) {
            return (String) value;

        } else if (typeParameterClass == Boolean.TYPE) {
            return Boolean.toString((Boolean) value);

        }
        return null;
    }

    public void setValueFromString(String newValue) {
        if (typeParameterClass == Double.TYPE) {
            try{
                value = (T) Double.valueOf(newValue);

            }catch(NumberFormatException e){
                value =  (T) Double.valueOf(-1);
            }
        } else if (typeParameterClass == Integer.TYPE) {
            try{
                value = (T) Integer.valueOf(newValue);

            }catch(NumberFormatException e){
                value =  (T) Integer.valueOf(-1);
            }

        } else if (typeParameterClass == String.class) {
            value = (T) String.valueOf(newValue);

        } else if (typeParameterClass == Boolean.TYPE) {
            value = (T) Boolean.valueOf(newValue);

        }

    }

}
