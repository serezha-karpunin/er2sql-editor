package com.etu.ui.ermodel.entity.node.attribute;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelEntityAttributeMediator extends AbstractMediator {
    private ObjectProperty<ERModelEntityAttribute> attribute = new SimpleObjectProperty<>();

    public void configure(ERModelEntityAttribute attribute) {
        this.attribute.set(attribute);
    }

    public ERModelEntityAttribute getAttribute() {
        return attribute.get();
    }

    public ObjectProperty<ERModelEntityAttribute> attributeProperty() {
        return attribute;
    }
}
