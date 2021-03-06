package com.notfound.makeamericafitagain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<Meal> mDataset;

    String image_name;

    ConstraintLayout layout_COLORME;

    TextView tv_food1;
    TextView tv_food2;
    TextView tv_food3;
    TextView tv_food4;
    TextView tv_food5;
    TextView tv_calories1;
    TextView tv_calories2;
    TextView tv_calories3;
    TextView tv_calories4;
    TextView tv_calories5;  
    ImageView iv_food1;
    ImageView iv_food2;
    ImageView iv_food3;
    ImageView iv_food4;
    ImageView iv_food5;

    TextView tv_title;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageRef;
    DatabaseReference refRoot;
    DatabaseReference refUser;

    int counter = 0;

    boolean isColor = false;

    List<String> adjs;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;
        public MyViewHolder(CardView cd) {
            super(cd);
            mCardView = cd;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(List<Meal> list_meals) {
        mDataset = list_meals;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        refRoot = FirebaseDatabase.getInstance().getReference();
        refUser = refRoot.child(mUser.getUid());
        storageRef = getInstance().getReference();

        adjsFill();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        CardView cd = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_meal, parent, false);

        MyViewHolder vh = new MyViewHolder(cd);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        layout_COLORME = holder.mCardView.findViewById(R.id.layout_COLORME);
        layout_COLORME.setBackgroundResource((isColor)?R.drawable.card_bg1:R.drawable.card_bg2);

        tv_title = holder.mCardView.findViewById(R.id.tv_title);
        tv_title.setText(randomAdjs() + " " + mDataset.get(position).getFood(0).getName());

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        tv_food1 = holder.mCardView.findViewById(R.id.tv_food1);
        tv_food1.setText(mDataset.get(position).getFood(0).getName());
        tv_food2 = holder.mCardView.findViewById(R.id.tv_food2);
        tv_food2.setText(mDataset.get(position).getFood(1).getName());
        tv_food3 = holder.mCardView.findViewById(R.id.tv_food3);
        tv_food3.setText(mDataset.get(position).getFood(2).getName());
        tv_food4 = holder.mCardView.findViewById(R.id.tv_food4);
        tv_food4.setText(mDataset.get(position).getFood(3).getName());
        tv_food5 = holder.mCardView.findViewById(R.id.tv_food5);
        tv_food5.setText(mDataset.get(position).getFood(4).getName());

        tv_food1.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_food2.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_food3.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_food4.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_food5.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));

        tv_title.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));

        tv_calories1 = holder.mCardView.findViewById(R.id.tv_calories1);
        tv_calories2 = holder.mCardView.findViewById(R.id.tv_calories2);
        tv_calories3 = holder.mCardView.findViewById(R.id.tv_calories3);
        tv_calories4 = holder.mCardView.findViewById(R.id.tv_calories4);
        tv_calories5 = holder.mCardView.findViewById(R.id.tv_calories5);

        tv_calories1.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_calories2.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_calories3.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_calories4.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));
        tv_calories5.setTextColor((isColor) ? Color.parseColor("#ffffff") : Color.parseColor("#333333"));

        tv_calories1 = holder.mCardView.findViewById(R.id.tv_calories1);
        tv_calories1.setText(mDataset.get(position).getFood(0).getCalories());
        tv_calories2 = holder.mCardView.findViewById(R.id.tv_calories2);
        tv_calories2.setText(mDataset.get(position).getFood(1).getCalories());
        tv_calories3 = holder.mCardView.findViewById(R.id.tv_calories3);
        tv_calories3.setText(mDataset.get(position).getFood(2).getCalories());
        tv_calories4 = holder.mCardView.findViewById(R.id.tv_calories4);
        tv_calories4.setText(mDataset.get(position).getFood(3).getCalories());
        tv_calories5 = holder.mCardView.findViewById(R.id.tv_calories5);
        tv_calories5.setText(mDataset.get(position).getFood(4).getCalories());

        holder.mCardView.setCardElevation((isColor)?1:20);

        String image_name = mDataset.get(position).getImage_name();
        switch(position){
            case 0:
                iv_food1 = holder.mCardView.findViewById(R.id.iv_food);
                break;
            case 1:
                iv_food2 = holder.mCardView.findViewById(R.id.iv_food);
                break;
            case 2:
                iv_food3 = holder.mCardView.findViewById(R.id.iv_food);
                break;
            case 3:
                iv_food4 = holder.mCardView.findViewById(R.id.iv_food);
                break;
            case 4:
                iv_food5 = holder.mCardView.findViewById(R.id.iv_food);
                break;
            default:
                break;
        }

        //retrieve image
        StorageReference imageRef = storageRef.child("images/" + image_name);

        try{
            final File imageFile = File.createTempFile("images", "jpg");

            imageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //SET IMAGEVIEW
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    // Get the dimensions of the View
                    int targetW = iv_food1.getWidth();
                    int targetH = iv_food1.getHeight();

                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;

                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    counter++;
                    switch(counter){
                        case 1:
                            iv_food1.setImageBitmap(rotatedBitmap);
                            break;
                        case 2:
                            iv_food2.setImageBitmap(rotatedBitmap);
                            break;
                        case 3:
                            iv_food3.setImageBitmap(rotatedBitmap);
                            break;
                        case 4:
                            iv_food4.setImageBitmap(rotatedBitmap);
                            break;
                        case 5:
                            iv_food5.setImageBitmap(rotatedBitmap);
                            break;
                        default:
                            break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch(IOException ie) {}



        isColor = !isColor;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void adjsFill(){
        adjs = new ArrayList<>();
        adjs.add("Flaming");
        adjs.add("Jaw-busting");
        adjs.add("Hot");
        adjs.add("Explosive");
        adjs.add("Home-made");
        adjs.add("Impossible");
        adjs.add("Hacking");
        adjs.add("Bitter");
        adjs.add("Bite-size");
        adjs.add("Chocolate");
        adjs.add("Classy");
        adjs.add("Crisp");
        adjs.add("Drizzled");
        adjs.add("Free");
        adjs.add("Glazed");
        adjs.add("Savoury");
    }

    public String randomAdjs(){
        adjsFill();
        Random random = new Random();
        int randomNum = random.nextInt((adjs.size() - 1) + 1) - 1;
        while(randomNum >= adjs.size() || randomNum < 0){
            randomNum = random.nextInt((adjs.size() - 1) + 1) - 1;
        }

        return adjs.get(randomNum);
    }
}
