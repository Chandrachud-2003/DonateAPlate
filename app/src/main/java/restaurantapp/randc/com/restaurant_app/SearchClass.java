package restaurantapp.randc.com.restaurant_app;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.suhel.library.ReelSearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.disposables.CompositeDisposable;
import restaurantapp.randc.com.restaurant_app.utils.RxUtils;

public class SearchClass extends AppCompatActivity {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private DictionaryManager mDictionaryManager;
    private SuggestionsAdapter mAdapter;

    private RecyclerView searchRecycler;
    private EditText searchBox;
    private ReelSearchView mReelSearchView;
    private TextView selectButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_search_class);
        mDictionaryManager = new DictionaryManager(this);
        mAdapter = new restaurantapp.randc.com.restaurant_app.SuggestionsAdapter(this);
        searchRecycler = findViewById(R.id.lstSuggestions);
        searchBox = findViewById(R.id.txtQuery);
        mReelSearchView = findViewById(R.id.reelSearch);
        selectButton = findViewById(R.id.btnSelect);



        searchRecycler.setAdapter(mAdapter);
        selectButton.setOnClickListener(v -> {
            final int selectedPosition =mReelSearchView.getLayoutManager().getSelection();
            Snackbar.make(selectButton,
                    "Selected position " + selectedPosition + " item " + mAdapter.getItem(selectedPosition),
                    Snackbar.LENGTH_SHORT).show();


        });
        searchBox.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> source.toString().toLowerCase().trim()
        });
        mReelSearchView.setOnSelectionChangedListener((prevSelection, newSelection) -> {
            Log.e("Selection", "Changed to " + newSelection + " from " + prevSelection);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDisposable.add(mDictionaryManager.loadDictionary()
                .doOnSubscribe(d -> {
                    searchBox.setEnabled(false);
                    searchBox.setHint("Loading dictionary");
                })
                .doOnComplete(() -> {
                    searchBox.setEnabled(true);
                    searchBox.setHint("Start typing");
                })
                .subscribe(() -> {
                }, Throwable::printStackTrace));

        mDisposable.add(RxUtils.onTextChange(searchBox)
                .filter(in -> mDictionaryManager.isLoaded())
                .concatMapSingle(in -> mDictionaryManager.query(in))
                .doOnNext(in -> selectButton.setEnabled(!in.isEmpty()))
                .subscribe(mAdapter::setData, Throwable::printStackTrace));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        mDisposable.clear();
    }




}
