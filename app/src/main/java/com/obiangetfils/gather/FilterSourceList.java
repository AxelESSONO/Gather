package com.obiangetfils.gather;

import android.widget.Filter;
import com.obiangetfils.gather.adapters.AdapterSourcelist;
import com.obiangetfils.gather.models.ModelSourceList;
import java.util.ArrayList;

public class FilterSourceList extends Filter {

    private AdapterSourcelist adapter;
    private ArrayList<ModelSourceList> filterList;

    // Constructor
    public FilterSourceList(AdapterSourcelist adapter, ArrayList<ModelSourceList> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        //Check Constant Validity
        if (constraint != null && constraint.length() > 0) {
            // Change to upper case
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelSourceList> filteredModel = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                    filteredModel.add(filterList.get(i));
                }
            }
            results.count = filteredModel.size();
            results.values = filteredModel;

        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.modelSourceLists = (ArrayList<ModelSourceList>) results.values;

        //refresh list
        adapter.notifyDataSetChanged();
    }
}
