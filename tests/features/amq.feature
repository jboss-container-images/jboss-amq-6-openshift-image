@jboss-amq-6/amq62-openshift
Feature: OpensShift AMQ image tests

  Scenario: Check that the labels are correctly set
    Given image is built
    Then the image should contain label com.redhat.component with value jboss-amq-6-amq62-openshift-docker
    And the image should contain label name with value jboss-amq-6/amq62-openshift

  # https://issues.jboss.org/browse/CLOUD-180
  Scenario: Check if image version and release is printed on boot
    When container is ready
    Then container log should contain Running jboss-amq-6/amq62-openshift image, version

