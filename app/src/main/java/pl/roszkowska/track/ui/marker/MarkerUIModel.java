package pl.roszkowska.track.ui.marker;

class MarkerUIModel extends BaseMarkerUIModel {
    final String markerName;
    final String markerTime;


    protected MarkerUIModel(long id, String markerName, String markerTime) {
        super(id);
        this.markerName = markerName;
        this.markerTime = markerTime;
    }
}
