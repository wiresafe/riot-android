/*
 * Copyright 2017 Vector Creations Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package venturetrac.wiresafe.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;

import java.util.Locale;

import venturetrac.wiresafe.R;
import venturetrac.wiresafe.VectorApp;
import venturetrac.wiresafe.adapters.LanguagesAdapter;
import venturetrac.wiresafe.util.ThemeUtils;

public class LanguagePickerActivity extends RiotAppCompatActivity implements LanguagesAdapter.OnSelectLocaleListener, SearchView.OnQueryTextListener {

    private View mLanguagesEmptyView;
    private LanguagesAdapter mAdapter;
    private SearchView mSearchView;

    /*
     * *********************************************************************************************
     * Static methods
     * *********************************************************************************************
     */

    public static Intent getIntent(final Context context) {
        return new Intent(context, LanguagePickerActivity.class);
    }

    /*
    * *********************************************************************************************
    * Activity lifecycle
    * *********************************************************************************************
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.settings_select_language);
        setContentView(R.layout.activity_langagues_picker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_languages_picker, menu);
        CommonActivityUtils.tintMenuIcons(menu, ThemeUtils.getColor(this, R.attr.icon_tint_on_dark_action_bar_color));

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setMaxWidth(Integer.MAX_VALUE);
            mSearchView.setSubmitButtonEnabled(false);
            mSearchView.setQueryHint(getString(R.string.search_hint));
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setOnQueryTextListener(this);
            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchAutoComplete.setHintTextColor(ThemeUtils.getColor(this, R.attr.default_text_hint_color));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(null);
        }
    }

    /*
    * *********************************************************************************************
    * UI
    * *********************************************************************************************
    */

    private void initViews() {
        mLanguagesEmptyView = findViewById(R.id.languages_empty_view);
        RecyclerView languagesRecyclerView = (RecyclerView) findViewById(R.id.languages_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        languagesRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LanguagesAdapter(VectorApp.getApplicationLocales(this), this);
        languagesRecyclerView.setAdapter(mAdapter);
    }

    private void filterLocales(final String pattern) {
        mAdapter.getFilter().filter(pattern, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                mLanguagesEmptyView.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
            }
        });
    }

    /*
    * *********************************************************************************************
    * Listener
    * *********************************************************************************************
    */

    @Override
    public void onSelectLocale(Locale locale) {
        VectorApp.updateApplicationLocale(locale);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterLocales(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

}