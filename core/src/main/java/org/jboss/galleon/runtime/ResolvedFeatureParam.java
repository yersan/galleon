/*
 * Copyright ${license.git.copyrightYears} Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.galleon.runtime;

import org.jboss.galleon.ProvisioningException;
import org.jboss.galleon.spec.FeatureParameterSpec;
import org.jboss.galleon.type.FeatureParameterType;

/**
 *
 * @author Alexey Loubyansky
 */
class ResolvedFeatureParam {

    final FeatureParameterSpec spec;
    final FeatureParameterType type;
    final Object defaultValue;

    ResolvedFeatureParam(FeatureParameterSpec spec, FeatureParameterType type) throws ProvisioningException {
        this.spec = spec;
        this.type = type;
        if(spec.hasDefaultValue()) {
            defaultValue = type.fromString(spec.getDefaultValue());
        } else {
            defaultValue = type.getDefaultValue();
        }
        if(spec.isFeatureId() && type.isCollection()) {
            throw new ProvisioningException("Feature ID parameter cannot be of a collection type");
        }
    }
}
