/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.client.support;

import java.util.List;

import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.google.common.base.Preconditions;

/**
 * Utility class providing methods to support processing collections of {@link Link links} returned in API resources.
 */
public class Links {

  /**
   * Returns the first link with a matching relation type in the supplied list of links.
   * 
   * @param links A list of {@link Link}.
   * @param relType The {@link LinkRelationType}.
   * @return The {@link Link} or null if a matching link was not found. 
   */
  public static Link findLinkWithType(List<Link> links, LinkRelationType relType) {
    Preconditions.checkNotNull(relType, "relType must not be null");
    if (links != null && links.size() > 0) {
      // TODO - In the future (e.g. on mandating Java 8 support) replace this with a functional filter method
      for (Link link : links) {
        if (relType.name().equals(link.getRel())) {
          return link;
        }
      }
    }
    return null;
  }
  
  /**
   * Specialisation of {@link #findLinkWithType(List, LinkRelationType)} providing a short-cut for the common task of 
   * finding a 'next' page link when paging through a resource collection.
   * 
   * @param links A list of {@link Link}.
   * @return The {@link Link} or null if a matching link was not found.
   * @see #findLinkWithType(List, LinkRelationType)
   */
  public static Link findNextPageLink(List<Link> links) {
    return Links.findLinkWithType(links, LinkRelationType.next);
  }
}