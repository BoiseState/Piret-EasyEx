import org.grouplens.lenskit.iterative.*
import org.grouplens.lenskit.knn.item.*
import org.grouplens.lenskit.mf.funksvd.*
import org.grouplens.lenskit.transform.normalize.*

trainTest {
    dataset crossfold("easyex") {
        source csvfile("easyex/BX-Book-Ratings_lenskit.csv_train2.csv") {
            delimiter "\t"
            domain {
                minimum 0.5
                maximum 5.0
                precision 0.5
            }
        }
    }

    algorithm("PersMean") {
        bind ItemScorer to UserMeanItemScorer
        bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer
    }

    algorithm("ItemItem") {
        bind ItemScorer to ItemItemScorer
        bind UserVectorNormalizer to BaselineSubtractingUserVectorNormalizer
        within (UserVectorNormalizer) {
            bind (BaselineScorer, ItemScorer) to ItemMeanRatingItemScorer
        }
    }

    algorithm("FunkSVD") {
        bind ItemScorer to FunkSVDItemScorer
        bind UserVectorNormalizer to BaselineSubtractingUserVectorNormalizer
        bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
        bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer
        set FeatureCount to 40
        set LearningRate to 0.002
        set IterationCount to 125
    }

    metric CoveragePredictMetric
    metric RMSEPredictMetric
    metric NDCGPredictMetric

    output "eval-results.csv"
    predictOutput "predictions.csv"
}
