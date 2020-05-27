package com.etu.ui.rmodel.relation.node.attribute;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RModelRelationAttributeMediator extends AbstractMediator {
    private ObjectProperty<RModelRelationAttribute> attribute = new SimpleObjectProperty<>();

    public void configure(RModelRelationAttribute attribute) {
        this.attribute.set(attribute);
    }

    public RModelRelationAttribute getAttribute() {
        return attribute.get();
    }

    public ObjectProperty<RModelRelationAttribute> attributeProperty() {
        return attribute;
    }
}
