package mariuszmaslanka.driverbook.driver;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DriverAggregationProvider {

  private static final int MAX_SIZE = 100;

  List<AggregationBuilder> getAggregations() {
    TermsAggregationBuilder byNationality = AggregationBuilders.terms("byNationality").field("nationality")
        .subAggregation(AggregationBuilders.sum("totalTitles").field("statistics.titles"))
        .subAggregation(AggregationBuilders.sum("totalWins").field("statistics.wins"))
        .subAggregation(AggregationBuilders.avg("avgRaces").field("statistics.races"))
        .subAggregation(topHitsSubAggregation());

    TermsAggregationBuilder byTitles = AggregationBuilders.terms("byTitles").field("statistics.titles")
        .subAggregation(topHitsSubAggregation());

    TermsAggregationBuilder byActivity = AggregationBuilders.terms("byActivity").field("active")
        .subAggregation(topHitsSubAggregation());

    return List.of(byNationality, byTitles, byActivity);
  }

  private TopHitsAggregationBuilder topHitsSubAggregation() {
    return AggregationBuilders.topHits("byHits").size(MAX_SIZE);
  }

}
