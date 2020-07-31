package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ramotion.fluidslider.FluidSlider;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Calendar;

import kotlin.Unit;

public class Bottom_Custom_Item extends BottomSheetDialogFragment {

    private Context mContext;
    private TextView categoryName;
    private FluidSlider weightSlider;
    private EditText nameText;
    private CardView deleteButton;
    private CardView finishButton;
    private ImageButton backButton;
    private TextView finishText;

    private int pos;
    private boolean newItem;

    private String tempName;
    private float tempWeight;

    private ButtonClickListener mButtonClickListener;

    private String category;

    private int total = 10;


    public Bottom_Custom_Item(Context context, int pos, float tempWeight, String tempName, String category)
    {
        mContext = context;
        this.pos = pos;
        newItem = false;
        this.tempName=tempName;
        this.tempWeight = tempWeight;
        this.category = category;

    }

    public Bottom_Custom_Item(Context context, String category)
    {
        mContext = context;
        newItem = true;
        this.category = category;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme_Contacts);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.custom_item_bottom_sheet, container, false);

        findViewsById(v);

        return v;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mButtonClickListener = (ButtonClickListener) context;

        }

        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+"Must Implement BottomSheetListener");
        }

    }

    private void findViewsById(View v) {

        categoryName = v.findViewById(R.id.customName);
        backButton = v.findViewById(R.id.backButton);
        nameText = v.findViewById(R.id.customItemName);
        weightSlider = v.findViewById(R.id.weightSlider);
        deleteButton = v.findViewById(R.id.deleteButton);
        finishButton = v.findViewById(R.id.saveButton);
        finishText = v.findViewById(R.id.saveText);

        weightSlider.setEndText("5kg");
        weightSlider.setStartText("0kg");
        categoryName.setText(category);


        if (newItem)
        {
            deleteButton.setVisibility(View.GONE);
            finishText.setText("Add Item");
            weightSlider.setPosition(0.0f);

        }
        else
        {
            deleteButton.setVisibility(View.VISIBLE);
            finishText.setText("Update Item");

            nameText.setText(tempName);
            weightSlider.setPosition(tempWeight/10.0f);
            weightSlider.setBubbleText(String.valueOf(tempWeight));
        }

        setOnClickListeners();


    }

    private void setOnClickListeners()
    {
        PushDownAnim.setPushDownAnimTo(backButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dismiss();
                    }
                });

        PushDownAnim.setPushDownAnimTo(deleteButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mButtonClickListener.deleteItem(pos);
                        dismiss();


                    }
                });

        PushDownAnim.setPushDownAnimTo(finishButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String name = nameText.getText().toString();
                        float current = (float)( Math.round(total * weightSlider.getPosition() *0.5 * 2) / 2.0);


                        if(newItem)
                        {
                            if ((!name.isEmpty()) && current>0.0f)
                            {
                                mButtonClickListener.addItem(name, current);
                                dismiss();
                            }
                            else if (name.isEmpty())
                            {
                                Toast.makeText(mContext, "Please enter name of the item", Toast.LENGTH_SHORT).show();
                            }

                            else if (current==0.0f)
                            {
                                Toast.makeText(mContext, "The item weight cannot be 0", Toast.LENGTH_SHORT).show();
                            }


                        }

                        else
                        {
                            if (!(name.isEmpty()) && !(current==0.0f)) {
                                if (!(name.equals(tempName)) || !(current == tempWeight)) {
                                    mButtonClickListener.updateItem(name, current, pos);
                                    dismiss();
                                } else {

                                    dismiss();
                                }
                            }
                            else if (name.isEmpty())
                            {
                                Toast.makeText(mContext, "Please enter name of the item", Toast.LENGTH_LONG).show();

                            }
                            else if (current==0.0f)
                            {
                                mButtonClickListener.deleteItem(pos);
                                dismiss();
                            }
                        }


                    }
                });


        weightSlider.setPositionListener(pos -> {
            final String value = String.valueOf( Math.round(total * weightSlider.getPosition() *0.5 * 2) / 2.0) ;
            weightSlider.setBubbleText(value);
            return Unit.INSTANCE;
        });


    }

    public interface ButtonClickListener
    {
        void deleteItem(int pos);
        void addItem(String name, double weight);
        void updateItem(String name, double weight, int pos);
    }
}
