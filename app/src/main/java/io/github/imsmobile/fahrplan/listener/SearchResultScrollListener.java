package io.github.imsmobile.fahrplan.listener;

import android.widget.AbsListView;

import ch.schoeb.opendatatransport.model.ConnectionQuery;
import io.github.imsmobile.fahrplan.SearchResultActivity;
import io.github.imsmobile.fahrplan.task.ConnectionSearchTask;

public class SearchResultScrollListener implements AbsListView.OnScrollListener {

    private final SearchResultActivity context;
    private final ConnectionQuery query;
    private final int visibleThreshold = 1;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    public SearchResultScrollListener(SearchResultActivity context, ConnectionQuery query) {
        this.context = context;
        this.query = query;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(loading) {
            if(totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            query.setPage(currentPage);
            new ConnectionSearchTask(context).execute(query);
            loading = true;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}
