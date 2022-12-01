/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import javax.inject.Inject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @param <T> the resulting type, the "Target"
 * @param <S> the input type, the "Source"
 *
 * @author Christian Autermann
 */
public abstract class AbstractDelegatingEncoder<T, S> implements Encoder<T, S> {

    private EncoderRepository encoderRepository;

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    @Inject
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    public <T, S> Encoder<T, S> getEncoder(EncoderKey key, EncoderKey... others) {
        return this.encoderRepository.getEncoder(key, others);
    }

}
