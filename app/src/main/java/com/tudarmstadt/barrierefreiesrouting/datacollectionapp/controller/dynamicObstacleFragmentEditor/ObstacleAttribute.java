package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;

import java.util.Observable;
import java.util.Observer;

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
    public ObstacleAttribute(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public void setValueFromString(String newValue) {
        if (typeParameterClass == Double.TYPE) {
            value = (T) Double.valueOf(newValue);

        } else if (typeParameterClass == Integer.TYPE) {
            value = (T) Integer.valueOf(newValue);

        } else if (typeParameterClass == String.class) {
            value = (T) String.valueOf(newValue);

        } else if (typeParameterClass == Boolean.TYPE) {
            value = (T) Boolean.valueOf(newValue);

        }

    }

}
