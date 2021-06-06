/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.shetland.ogc.om.values;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;

public class HrefAttributeValue
        implements Value<W3CHrefAttribute> {

    private W3CHrefAttribute value;

    /**
     * Unit of measure
     */
    private UoM unit;

    public HrefAttributeValue() {
    }

    public HrefAttributeValue(W3CHrefAttribute value) {
        setValue(value);
    }

    @Override
    public HrefAttributeValue setValue(W3CHrefAttribute value) {
        this.value = value;
        return this;
    }

    @Override
    public W3CHrefAttribute getValue() {
        return value;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public HrefAttributeValue setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String getUnit() {
        if (isSetUnit()) {
            return unit.getUom();
        }
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return this.unit;
    }

    @Override
    public boolean isSetValue() {
        return getValue() != null && getValue().isSetHref();
    }

    @Override
    public String toString() {
        return String.format("HrefAttributeValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
