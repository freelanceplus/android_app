package com.ciit.freelanceplus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciit.freelanceplus.Model.SkillModel;
import com.ciit.freelanceplus.Model.Utils;
import com.ciit.freelanceplus.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchSkillActivity extends AppCompatActivity {

    ArrayList<SkillModel> skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_skill);

        ImageView back = findViewById(R.id.search_skill_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final ListView listView = findViewById(R.id.search_skills_listview);
        final ProgressDialog dialog = new ProgressDialog(SearchSkillActivity.this);
        dialog.setMessage("Fetching data ...");
        dialog.show();


        String URL = getResources().getString(R.string.API_URL)+"/freelancePlus_API/public/skills";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {   skills = new ArrayList<>();

                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        SkillModel model = new SkillModel();
                        model.id = object.getInt("id");
                        model.title= object.getString("title");
                        model.created_at = object.getString("created_at");
                        model.updated_at = object.getString("updated_at");

                        skills.add(model);
                    }
                    dialog.dismiss();
                    SearchSkillAdapter adapter = new SearchSkillAdapter(SearchSkillActivity.this, skills);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Utils.currentSkill = skills.get(i);
                            QuestionsActivity.projectType.setText(skills.get(i).title);
                            onBackPressed();

                        }
                    });


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Toast.makeText(SearchSkillActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(SearchSkillActivity.this);
        queue.add(request);
    }
}

class SearchSkillAdapter extends BaseAdapter{

    Context context;
    ArrayList<SkillModel> models;
    LayoutInflater layoutInflater;

    SearchSkillAdapter(Context context, ArrayList<SkillModel> models)
    {
        this.context = context;
        this.models = models;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root;
        if(view == null)
        {
            root = layoutInflater.inflate(R.layout.skill_model, null);
        }else {
            root = view;
        }

        TextView name = root.findViewById(R.id.skill_model_name);
        name.setText(models.get(i).title);

        return root;
    }
}
