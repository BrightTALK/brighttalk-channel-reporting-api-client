/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brighttalk.channels.reportingapi.client;

import com.brighttalk.channels.reportingapi.client.common.NextPageUrl;
import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.support.LinkRelationType;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Encapsulates the paging criteria for all paged reporting APIs, e.g. those which return collections of resources.
 * 
 * @author Neil Brown
 */
public class PageCriteria {
  private final Integer pageSize;
  private final Link nextPageLink;

  /**
   * Creates page criteria for a request for the first page of resources.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null then API
   * requests built from this criteria won't include a page size, instead relying on the API's default page size.
   */
  public PageCriteria(Integer pageSize) {
    this(pageSize, null);
  }

  /**
   * Creates page criteria for the next page of resources, using the default page size.
   * 
   * @param nextPageLink A {@link Link} to the next page of a collection of resources, as returned in the previous page
   * of the resource collection. Must have a relation type ('rel') of {@link LinkRelationType#next} and a href
   * containing a valid URL for a next page of resources. Optional, only supplied for API calls for secondary pages of
   * resources.
   */
  public PageCriteria(Link nextPageLink) {
    this(null, nextPageLink);
  }

  /**
   * Creates page criteria for the next page of resources, with a specified page size.
   * 
   * @param pageSize The max no. of entries to return in the requested page of resources. Optional. If null then API
   * requests built from this criteria won't include a page size, instead relying on the API's default page size.
   * @param nextPageLink A {@link Link} to the next page of a collection of resources, as returned in the previous page
   * of the resource collection. Must have a relation type ('rel') of {@link LinkRelationType#next} and a href
   * containing a valid URL for a next page of resources. Optional, only supplied for API calls for secondary pages of
   * resources.
   */
  public PageCriteria(Integer pageSize, Link nextPageLink) {
    Preconditions.checkArgument(pageSize == null || pageSize > 0, "pageSize must be null or greater than zero.");
    if (nextPageLink != null) {
      try {
        NextPageUrl.parse(nextPageLink.getHref());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("nextPageLink href is not a valid next page URL [" + nextPageLink + "].", e);
      }
    }
    this.pageSize = pageSize;
    this.nextPageLink = nextPageLink;
  }

  public final Integer getPageSize() {
    return this.pageSize;
  }

  public final Link getNextPageLink() {
    // Construct a copy of link field to preserve immutability
    return this.nextPageLink != null ?
        new Link(this.nextPageLink.getHref(), this.nextPageLink.getRel(), this.nextPageLink.getTitle()) : null;
  }

  @Override
  public String toString() {
    /* @formatter:off */
    return Objects.toStringHelper(this).omitNullValues()
      .add("pageSize", this.pageSize)
      .add("nextPageLink", this.nextPageLink)
      .toString();
    /* @formatter:on */
  }
}