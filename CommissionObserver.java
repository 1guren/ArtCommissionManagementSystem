package finals;

// Observer Pattern: Interface that all observers must implement
// Any panel that wants to be notified of commission changes implements this
public interface CommissionObserver {
    void onCommissionUpdated(CommissionRequest request);
}