/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

import java.util.Objects;
import java.util.Set;

/**
 * Class to hold Query Parameters
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptions {

    private static Long DEFAULT_TOP = 100L;

    private String baseURL;

    private CountFilter countOption;
    private OrderByFilter orderByOption;
    private SelectFilter selectOption;
    private ExpandFilter expandOption;
    private SkipTopFilter skipOption;
    private SkipTopFilter topOption;
    private FilterFilter filterOption;

    public QueryOptions(String baseURL, Set<FilterClause> queryOptions) {
        this.baseURL = baseURL;
        if (queryOptions != null) {
            queryOptions.forEach(input -> {
                if (input instanceof CountOption) {
                    countOption = (CountFilter) input;
                } else if (input instanceof OrderByOption) {
                    orderByOption = (OrderByFilter) input;
                } else if (input instanceof SelectOption) {
                    selectOption = (SelectFilter) input;
                } else if (input instanceof ExpandOption) {
                    expandOption = (ExpandFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Skip)) {
                    skipOption = (SkipTopFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Top)) {
                    topOption = (SkipTopFilter) input;
                } else if (input instanceof FilterOption) {
                    filterOption = (FilterFilter) input;
                }
            });
        }
        if (!hasTopOption()) {
            this.topOption = new SkipTopFilter(SkipTopOperator.Top, DEFAULT_TOP);
        }
    }

    public String getBaseURI() {
        return baseURL;
    }

    public boolean hasCountOption() {
        return countOption != null;
    }

    public CountFilter getCountOption() {
        return countOption;
    }

    public boolean hasTopOption() {
        return topOption != null;
    }

    public SkipTopFilter getTopOption() {
        return topOption;
    }

    public boolean hasSkipOption() {
        return skipOption != null;
    }

    public SkipTopFilter getSkipOption() {
        return skipOption;
    }

    public boolean hasOrderByOption() {
        return orderByOption != null;
    }

    public OrderByOption getOrderByOption() {
        return orderByOption;
    }

    public boolean hasSelectOption() {
        return selectOption != null;
    }

    public SelectOption getSelectOption() {
        return selectOption;
    }

    public boolean hasExpandOption() {
        return expandOption != null;
    }

    public ExpandOption getExpandOption() {
        return expandOption;
    }

    public boolean hasFilterOption() {
        return filterOption != null;
    }

    public FilterOption getFilterOption() {
        return filterOption;
    }

    @Override public int hashCode() {
        return Objects.hash(baseURL,
                            countOption,
                            topOption,
                            skipOption,
                            orderByOption,
                            selectOption,
                            expandOption,
                            filterOption);
    }

    // BaseURI is not always set -> we need to compare each option individually
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QueryOptions)) {
            return false;
        }
        QueryOptions obj = (QueryOptions) o;
        return obj.hasCountOption() == this.hasCountOption()
                && obj.hasOrderByOption() == this.hasOrderByOption()
                && obj.hasSelectOption() == this.hasSelectOption()
                && obj.hasExpandOption() == this.hasExpandOption()
                && obj.hasSkipOption() == this.hasSkipOption()
                && obj.hasTopOption() == this.hasTopOption()
                && obj.hasFilterOption() == this.hasFilterOption()
                && Objects.equals(obj.getCountOption(), this.getCountOption())
                && Objects.equals(obj.getOrderByOption(), this.getOrderByOption())
                && Objects.equals(obj.getSelectOption(), this.getSelectOption())
                && Objects.equals(obj.getExpandOption(), this.getExpandOption())
                && Objects.equals(obj.getSkipOption(), this.getSkipOption())
                && Objects.equals(obj.getTopOption(), this.getTopOption())
                && Objects.equals(obj.getFilterOption(), this.getFilterOption());
    }
}
