package com.kickstarter.viewmodels;

import android.support.annotation.NonNull;

import com.kickstarter.KSRobolectricTestCase;
import com.kickstarter.factories.ProjectFactory;
import com.kickstarter.factories.ProjectStatsFactory;
import com.kickstarter.factories.ProjectsEnvelopeFactory;
import com.kickstarter.libs.Environment;
import com.kickstarter.models.Project;
import com.kickstarter.models.ProjectStats;
import com.kickstarter.services.MockApiClient;
import com.kickstarter.services.apiresponses.ProjectsEnvelope;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

public class CreatorDashboardHeaderHolderViewModelTest extends KSRobolectricTestCase {
  private CreatorDashboardHeaderHolderViewModel.ViewModel vm;

  private final TestSubscriber<String> projectBackersCountText = new TestSubscriber<>();

  protected void setUpEnvironment(final @NonNull Environment environment) {
    this.vm = new CreatorDashboardHeaderHolderViewModel.ViewModel(environment);
    this.vm.outputs.projectBackersCountText().subscribe(this.projectBackersCountText);


  }

  @Test
  public void testProjectBackersCountText() {
    final Project project = ProjectFactory.project().toBuilder().backersCount(10).build();
    final ProjectStats projectStats = ProjectStatsFactory.projectStats();
    this.vm = new CreatorDashboardHeaderHolderViewModel.ViewModel(environment());
    this.vm.outputs.projectBackersCountText().subscribe(this.projectBackersCountText);
    this.vm.inputs.projectAndStats(project, projectStats);
    this.projectBackersCountText.assertValues("10");
  }
}
