package uk.gov.hmrc.jenkinsjobbuilders.domain.publisher

import javaposse.jobdsl.dsl.Job
import spock.lang.Specification
import uk.gov.hmrc.jenkinsjobbuilders.domain.builder.JobBuilder
import uk.gov.hmrc.jenkinsjobbuilders.domain.JobParents

import static uk.gov.hmrc.jenkinsjobbuilders.domain.publisher.PostBuildTaskPublisher.postBuildTaskPublisher

@Mixin(JobParents)
class PostBuildTaskPublisherSpec extends Specification {

    void 'test XML output'() {
        given:
        JobBuilder jobBuilder = new JobBuilder('test-job', 'test-job-description').
                                               withPublishers(postBuildTaskPublisher("test-log-text", 'test-script'))

        when:
        Job job = jobBuilder.build(jobParent())

        then:
        with(job.node) {
            publishers.'hudson.plugins.postbuildtask.PostbuildTask'.tasks.'hudson.plugins.postbuildtask.TaskProperties'.logTexts.'hudson.plugins.postbuildtask.LogProperties'.logText.text() == 'test-log-text'
            publishers.'hudson.plugins.postbuildtask.PostbuildTask'.tasks.'hudson.plugins.postbuildtask.TaskProperties'.EscalateStatus.text() == 'true'
            publishers.'hudson.plugins.postbuildtask.PostbuildTask'.tasks.'hudson.plugins.postbuildtask.TaskProperties'.RunIfJobSuccessful.text() == 'false'
            publishers.'hudson.plugins.postbuildtask.PostbuildTask'.tasks.'hudson.plugins.postbuildtask.TaskProperties'.script.text() == 'test-script'
        }
    }
}
