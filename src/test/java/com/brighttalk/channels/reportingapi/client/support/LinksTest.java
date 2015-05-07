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
package com.brighttalk.channels.reportingapi.client.support;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.support.LinkRelationType;
import com.brighttalk.channels.reportingapi.client.support.Links;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link Links}.
 */
public class LinksTest {

  private static final String LINK_HREF = "http://api.test.brighttalk.net/foo";

  @Before
  public void setUp() throws Exception {
  }

  /**
   * Tests {@link Links#findLinkWithType(List, LinkRelationType)} in the case where the supplied list is null.
   */
  @Test
  public final void testFindLinkWithTypeWhenLinksNull() {
    Link link = Links.findLinkWithType(null, LinkRelationType.next);
    assertThat(link, nullValue());
  }

  /**
   * Tests {@link Links#findLinkWithType(List, LinkRelationType)} in the case where the supplied list is empty.
   */
  @Test
  public final void Empty() {
    Link link = Links.findLinkWithType(Collections.<Link>emptyList(), LinkRelationType.next);
    assertThat(link, nullValue());
  }

  /**
   * Tests {@link Links#findLinkWithType(List, LinkRelationType)} in the case where the type of link does exist in the
   * supplied list of links.
   */
  @Test
  public final void testFindLinkWithTypeWhenLinkExists() {
    LinkRelationType relType = LinkRelationType.channel_subscribers;
    Link expectedLink = new Link(LINK_HREF, relType.name());
    List<Link> links = ImmutableList.of(new Link(LINK_HREF, LinkRelationType.surveys.name()),
        expectedLink, new Link(LINK_HREF, LinkRelationType.webcasts.name()));

    Link actualLink = Links.findLinkWithType(links, relType);

    assertThat(actualLink, is(expectedLink));
  }

  /**
   * Tests {@link Links#findLinkWithType(List, LinkRelationType)} in the case where the type of link does NOT exist in
   * the supplied list of links.
   */
  @Test
  public final void testFindLinkWithTypeWhenLinkDoesNotExist() {
    LinkRelationType relType = LinkRelationType.next;
    List<Link> links = ImmutableList.of(new Link(LINK_HREF, LinkRelationType.surveys.name()),
        new Link(LINK_HREF, LinkRelationType.channel_subscribers.name()),
        new Link(LINK_HREF, LinkRelationType.webcasts.name()));

    Link actualLink = Links.findLinkWithType(links, relType);

    assertThat(actualLink, nullValue());
  }

  /**
   * Tests {@link Links#find#findNextPageLink(List)} in the case where the supplied list of links does include a next
   * page link.
   */
  @Test
  public final void testFindNextPageLinkWhenLinkExists() {
    Link expectedLink = new Link(LINK_HREF, LinkRelationType.next.name());
    List<Link> links = ImmutableList.of(expectedLink);

    Link actualLink = Links.findNextPageLink(links);

    assertThat(actualLink, is(expectedLink));
  }
}