/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationByIdResponse;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class SosV1GetObservationByIdResponseEncoder extends AbstractSosV1ResponseEncoder<GetObservationByIdResponse> {
    public SosV1GetObservationByIdResponseEncoder() {
        super(SosConstants.Operations.GetObservationById.name(), GetObservationByIdResponse.class);
    }

    @Override
    protected XmlObject create(GetObservationByIdResponse response) throws EncodingException {
        return getEncoder(response.getResponseFormat(), response).encode(response);
    }
}