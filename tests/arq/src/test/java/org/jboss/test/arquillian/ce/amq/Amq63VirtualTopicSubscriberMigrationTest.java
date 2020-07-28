/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test.arquillian.ce.amq;

import java.io.IOException;


import org.arquillian.cube.openshift.api.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.arquillian.ce.amq.support.AmqMigrationTestBase;
import org.jboss.test.arquillian.ce.amq.support.AmqVirtualTopicSubscriberMigrationTestBase;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
@Template(url = "https://raw.githubusercontent.com/${template.repository:jboss-openshift}/application-templates/${template.branch:master}/amq/amq63-persistent.json",
        parameters = {
                @TemplateParameter(name = "IMAGE_STREAM_NAMESPACE", value = "${kubernetes.namespace:openshift}"),
                @TemplateParameter(name = "MQ_QUEUES", value = "QUEUES.FOO,QUEUES.BAR"),
                @TemplateParameter(name = "MQ_TOPICS", value = "topics.mqtt,TOPICS.FOO"),
                @TemplateParameter(name = "APPLICATION_NAME", value = "amq-test"),
                @TemplateParameter(name = "AMQ_MESH_QUERY_INTERVAL", value = "5"), // needs to be respected to the template
                @TemplateParameter(name = "MQ_USERNAME", value = "${amq.username:amq-test}"),
                @TemplateParameter(name = "MQ_PASSWORD", value = "${amq.password:redhat}"),
                @TemplateParameter(name = "MQ_PROTOCOL", value = "openwire,amqp,mqtt,stomp")})
@RoleBinding(roleRefName = "view", userName = "system:serviceaccount:${kubernetes.namespace}:default")
@OpenShiftResource("classpath:testrunner-secret.json")
@OpenShiftResource("classpath:testrunner-secret.json")
@Replicas(1)
//@Ignore("https://github.com/jboss-openshift/ce-testsite/issues/123")
// there is an issue with being ready before being networked into the mesh - remote demand won't
// be visible and vt fanout will drop messages for remote consumers
// AMQ_MESH_QUERY_INTERVAL needs to be in the template to allow mesh discovery to be snappy and
// the test should verify the presence of network bridges - or the readyness probe should
// some sleeps in there for the moment
public class Amq63VirtualTopicSubscriberMigrationTest extends AmqVirtualTopicSubscriberMigrationTestBase {

    @Deployment
    public static WebArchive getDeployment() throws IOException {
        return getDeploymentBase(AmqMigrationTestBase.class, AmqVirtualTopicSubscriberMigrationTestBase.class);
    }
}