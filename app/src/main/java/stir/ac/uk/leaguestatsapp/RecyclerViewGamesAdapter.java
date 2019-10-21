package stir.ac.uk.leaguestatsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewGamesAdapter extends RecyclerView.Adapter<RecyclerViewGamesAdapter.ViewHolder> {

    private ArrayList<String> champNames = new ArrayList<>();
    private ArrayList<String> champImageNames = new ArrayList<>();
    private ArrayList<String> roleNames = new ArrayList<>();
    private ArrayList<String> killList = new ArrayList<>();
    private ArrayList<String> deathList = new ArrayList<>();
    private ArrayList<String> assistList = new ArrayList<>();
    private ArrayList<Integer> sumspell1List;
    private ArrayList<Integer> sumspell2List;
    private ArrayList<Integer> runePrimList;
    private ArrayList<Integer> runeSecList;
    private ArrayList<Integer> item1List;
    private ArrayList<Integer> item2List;
    private ArrayList<Integer> item3List;
    private ArrayList<Integer> item4List;
    private ArrayList<Integer> item5List;
    private ArrayList<Integer> item6List;
    private ArrayList<Boolean> victoryList;
    private ArrayList<Long> durationList;
    private Context mContext;

    public RecyclerViewGamesAdapter(ArrayList<String> champNames,
                                    ArrayList<String> champImageNames,
                                    ArrayList<String> roleNames,
                                    ArrayList<String> killList,
                                    ArrayList<String> deathList,
                                    ArrayList<String> assistList,
                                    ArrayList<Integer> sumspell1List,
                                    ArrayList<Integer> sumspell2List,
                                    ArrayList<Integer> runePrimList,
                                    ArrayList<Integer> runeSecList,
                                    ArrayList<Integer> item1List,
                                    ArrayList<Integer> item2List,
                                    ArrayList<Integer> item3List,
                                    ArrayList<Integer> item4List,
                                    ArrayList<Integer> item5List,
                                    ArrayList<Integer> item6List,
                                    ArrayList<Boolean> victoryList,
                                    ArrayList<Long> durationList,
                                    Context mContext) {

        this.champNames = champNames;
        this.champImageNames = champImageNames;
        this.roleNames = roleNames;
        this.killList = killList;
        this.deathList = deathList;
        this.assistList = assistList;
        this.sumspell1List = sumspell1List;
        this.sumspell2List = sumspell2List;
        this.runePrimList = runePrimList;
        this.runeSecList = runeSecList;
        this.item1List = item1List;
        this.item2List = item2List;
        this.item3List = item3List;
        this.item4List = item4List;
        this.item5List = item5List;
        this.item6List = item6List;
        this.victoryList = victoryList;
        this.durationList = durationList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gameitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        int champImgName = mContext.getResources().getIdentifier("" + champImageNames.get(position).toLowerCase(), "drawable", mContext.getPackageName());
        holder.champSquare.setImageResource(champImgName);

        int sumSpell1Name = mContext.getResources().getIdentifier("s_" + sumspell1List.get(position), "drawable", mContext.getPackageName());
        holder.summoner1.setImageResource(sumSpell1Name);

        int sumSpell2Name = mContext.getResources().getIdentifier("s_" + sumspell2List.get(position), "drawable", mContext.getPackageName());
        holder.summoner2.setImageResource(sumSpell2Name);

        int runePrimName = mContext.getResources().getIdentifier("r_" + runePrimList.get(position), "drawable", mContext.getPackageName());
        holder.rune1.setImageResource(runePrimName);

        int runeSecName = mContext.getResources().getIdentifier("r_" + runeSecList.get(position), "drawable", mContext.getPackageName());
        holder.rune2.setImageResource(runeSecName);

        int item1Name = mContext.getResources().getIdentifier("i_" + item1List.get(position), "drawable", mContext.getPackageName());
        holder.item1.setImageResource(item1Name);

        int item2Name = mContext.getResources().getIdentifier("i_" + item2List.get(position), "drawable", mContext.getPackageName());
        holder.item2.setImageResource(item2Name);

        int item3Name = mContext.getResources().getIdentifier("i_" + item3List.get(position), "drawable", mContext.getPackageName());
        holder.item3.setImageResource(item3Name);

        int item4Name = mContext.getResources().getIdentifier("i_" + item4List.get(position), "drawable", mContext.getPackageName());
        holder.item4.setImageResource(item4Name);

        int item5Name = mContext.getResources().getIdentifier("i_" + item5List.get(position), "drawable", mContext.getPackageName());
        holder.item5.setImageResource(item5Name);

        int item6Name = mContext.getResources().getIdentifier("i_" + item6List.get(position), "drawable", mContext.getPackageName());
        holder.item6.setImageResource(item6Name);

/*
        if (roleNames.get(position) == null || roleNames.get(position).equals("NONE")) {
            holder.roleIcon.setImageResource(R.drawable.nonepos);
        } else {
            int roleImgName = mContext.getResources().getIdentifier("" + roleNames.get(position).toLowerCase(), "drawable", mContext.getPackageName());
            holder.roleIcon.setImageResource(roleImgName);
        }
*/


        if (victoryList.get(position)) {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#805582d1"));
            holder.result.setText("VICTORY");
        } else {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#80d15557"));
            holder.result.setText("DEFEAT");
        }
            holder.champName.setText(champNames.get(position));

            int kills = Integer.parseInt(killList.get(position));
            int deaths = Integer.parseInt(deathList.get(position));
            int assists = Integer.parseInt(assistList.get(position));
            holder.kda.setText(kills+" / "+deaths+" / " +assists);
            double kdr = ((double) kills + (double) assists) / (double) deaths;
            DecimalFormat df = new DecimalFormat("###.##");
            holder.kdr.setText(df.format(kdr));
            long duration = durationList.get(position);
            long mins = (duration/60);
            long seconds = (duration % 60);
            holder.duration.setText(""+mins+"m "+seconds+"s");

        holder.champSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChampionStats.class);
                intent.putExtra("PASSED_CHAMPNAME_NAME", champNames.get(position));
                System.out.println("Passed champion name: "+champNames.get(position));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return champNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView champSquare;
        //ImageView roleIcon;
        ImageView summoner1;
        ImageView summoner2;
        ImageView rune1;
        ImageView rune2;
        ImageView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        ImageView item5;
        ImageView item6;

        TextView champName;
        TextView kda;
        TextView kdr;
        TextView result;
        TextView duration;

        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            champSquare = itemView.findViewById(R.id.champSquare);
            //roleIcon = itemView.findViewById(R.id.summoner1);
            summoner1 = itemView.findViewById(R.id.summoner1);
            summoner2 = itemView.findViewById(R.id.summoner2);

            rune1 = itemView.findViewById(R.id.rune1);
            rune2 = itemView.findViewById(R.id.rune2);

            item1 = itemView.findViewById(R.id.Citem1);
            item2 = itemView.findViewById(R.id.Citem2);
            item3 = itemView.findViewById(R.id.Citem3);
            item4 = itemView.findViewById(R.id.item4);
            item5 = itemView.findViewById(R.id.item5);
            item6 = itemView.findViewById(R.id.item6);

            champName = itemView.findViewById(R.id.champName);
            kda = itemView.findViewById(R.id.kda);
            kdr = itemView.findViewById(R.id.kdr);

            result = itemView.findViewById(R.id.result);
            duration = itemView.findViewById(R.id.duration);

            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}