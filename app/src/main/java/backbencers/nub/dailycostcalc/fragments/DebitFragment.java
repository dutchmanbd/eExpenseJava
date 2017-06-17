

package backbencers.nub.dailycostcalc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import backbencers.nub.dailycostcalc.R;
import backbencers.nub.dailycostcalc.activities.DebitEditorActivity;
import backbencers.nub.dailycostcalc.adapter.DebitListAdapter;
import backbencers.nub.dailycostcalc.database.ExpenseDataSource;
import backbencers.nub.dailycostcalc.model.Debit;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitFragment extends Fragment {

    List<Debit> debitList;
    ExpenseDataSource expenseDataSource;

    public DebitFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_debit, container, false);

        getActivity().setTitle("Debit");

        ListView debitListView = (ListView) view.findViewById(R.id.lv_debits);
        TextView debitEmptyView = (TextView) view.findViewById(R.id.empty_view_debit);

        debitListView.setEmptyView(debitEmptyView);

        FloatingActionButton fabDebit = (FloatingActionButton) view.findViewById(R.id.fab_debit);
        fabDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DebitEditorActivity.class);
                startActivity(intent);
            }
        });

        expenseDataSource = new ExpenseDataSource(getContext());
        debitList = expenseDataSource.getAllDebits();

        DebitListAdapter adapter = new DebitListAdapter(getContext(), debitList);

        debitListView.setAdapter(adapter);

        return view;
    }

}
