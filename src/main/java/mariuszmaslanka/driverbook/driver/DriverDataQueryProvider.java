package mariuszmaslanka.driverbook.driver;

import lombok.RequiredArgsConstructor;
import mariuszmaslanka.driverbook.api.Filters;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

import static java.time.LocalDate.of;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.YearMonth.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Component
@RequiredArgsConstructor
class DriverDataQueryProvider {

  private static final int LAST_DAY_OF_MONTH = 31;
  private static final int FIRST_DAY_OF_MONTH = 1;
  public static final int YEAR_1800 = 1800;
  private final Clock clock;

  QueryBuilder getQuery(Filters filters) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

    if (nonNull(filters.getActive())) {
      boolQueryBuilder.filter(termQuery("active", filters.getActive()));
    }

    if (nonNull(filters.getNationality())) {
      boolQueryBuilder.filter(termQuery("nationality", filters.getNationality()));
    }

    LocalDate from = isNull(filters.getBirthFrom()) ? beginOfYear(YEAR_1800) : beginOfYear(filters.getBirthFrom());
    LocalDate to = isNull(filters.getBirthTo()) ? endOfYear(now(clock).getYear()) : endOfYear(filters.getBirthTo());

    boolQueryBuilder.filter(rangeQuery("dateOfBirth")
        .gte(from).lte(to).includeLower(true).includeUpper(true));

    return boolQueryBuilder;
  }

  private LocalDate beginOfYear(Integer year) {
    return of(year, JANUARY, FIRST_DAY_OF_MONTH);
  }

  private LocalDate endOfYear(int year) {
    return of(year, DECEMBER, LAST_DAY_OF_MONTH);
  }
}
