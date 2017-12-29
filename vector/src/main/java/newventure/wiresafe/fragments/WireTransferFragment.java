package newventure.wiresafe.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import newventure.wiresafe.Matrix;
import newventure.wiresafe.R;
import newventure.wiresafe.activity.CommonActivityUtils;
import newventure.wiresafe.activity.VectorRoomActivity;
import newventure.wiresafe.util.VectorUtils;

public class WireTransferFragment extends AbsHomeFragment {
    public static final String WIRE_TRANSFER = "wireTransfer";
    private static final String TAG = WireTransferFragment.class.getSimpleName();
    protected MXSession mSession;
    private MaterialSpinner mChatRoomsSpinner;
    private ArrayList<Room> mChatRooms;

    private EditText mBankNameEditText;
    private EditText mBankAddressEditText;
    private EditText mAccountOwnerNameEditText;
    private EditText mBankRoutingNumberEditText;
    private EditText mBankAccountNumberEditText;
    private AlertDialog mNotMemberAlert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wire_transfer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheWidget(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPrimaryColor = ContextCompat.getColor(getActivity(), R.color.tab_home);
        mSecondaryColor = ContextCompat.getColor(getActivity(), R.color.tab_home_secondary);
    }

    @Override
    protected List<Room> getRooms() {
        return null;
    }

    @Override
    protected void onFilter(String pattern, OnFilterListener listener) {

    }

    @Override
    protected void onResetFilter() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSession = Matrix.getInstance(context).getDefaultSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeNotMemberAlert();
    }

    private void removeNotMemberAlert() {
        if (null != mNotMemberAlert && mNotMemberAlert.isShowing())
            mNotMemberAlert.dismiss();
    }

    private void initData() {
        final Collection<Room> roomCollection = mSession.getDataHandler().getStore().getRooms();
        final List<String> directChatIds = mSession.getDirectChatRoomIdsList();
        mChatRooms = new ArrayList<>();
        ArrayList<Object> spinnerRoomList = new ArrayList<>();

        for (Room room : roomCollection) {
            if (!room.isConferenceUserRoom() && !room.isInvited() && !room.isDirectChatInvitation()) {
                // it seems that the server syncs some left rooms
                if (null == room.getMember(mSession.getMyUserId())) {
                    Log.e(TAG, "## initData(): invalid room " + room.getRoomId() + ", the user is not anymore member of it");
                } else {
                    if (!directChatIds.contains(room.getRoomId())) {
                        mChatRooms.add(room);
                        spinnerRoomList.add(VectorUtils.getRoomDisplayName(getContext(), mSession, room));
                    }
                }
            }
        }
        mChatRoomsSpinner.setItems(spinnerRoomList);
        if (mChatRooms.isEmpty())
            showNotAddedToRoom();
        else
            mChatRoomsSpinner.setSelectedIndex(0);
    }

    private void cacheWidget(View view) {
        AppCompatButton buttonSubmit = (AppCompatButton) view.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInput()) {
                    sendWiretransferDetails(getWireTransferDetails());
                } else {
                    Toast.makeText(getActivity(), "Enter valid bank details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView resetTextView = (TextView) view.findViewById(R.id.textview_reset);
        resetTextView.setPaintFlags(resetTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWireTransferDetails();
            }
        });
        mChatRoomsSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_chat_rooms);

        mBankNameEditText = (EditText) view.findViewById(R.id.input_bank_name);
        mBankAddressEditText = (EditText) view.findViewById(R.id.input_bank_address);
        mAccountOwnerNameEditText = (EditText) view.findViewById(R.id.input_account_owner_name);
        mBankRoutingNumberEditText = (EditText) view.findViewById(R.id.input_bank_routing_number);
        mBankAccountNumberEditText = (EditText) view.findViewById(R.id.input_bank_account_number);
    }

    private String getWireTransferDetails() {
        return "Bank Account Details:\n\tBank Name : " + mBankNameEditText.getText().toString().trim() +
                "\n\tBank Address : " + mBankAddressEditText.getText().toString().trim() +
                "\n\tAccount Owner Name : " + mAccountOwnerNameEditText.getText().toString().trim() +
                "\n\tBank Routing Number : " + mBankRoutingNumberEditText.getText().toString().trim() +
                "\n\tBank Account Number : " + mBankAccountNumberEditText.getText().toString().trim();
    }

    private boolean validInput() {
        boolean validInput = true;
        if (mBankNameEditText.getText().toString().trim().isEmpty())
            validInput = false;
        else if (mBankAddressEditText.getText().toString().trim().isEmpty())
            validInput = false;
        else if (mAccountOwnerNameEditText.getText().toString().trim().isEmpty())
            validInput = false;
        else if (mBankRoutingNumberEditText.getText().toString().trim().isEmpty())
            validInput = false;
        else if (mBankAccountNumberEditText.getText().toString().trim().isEmpty())
            validInput = false;
        return validInput;
    }

    private void sendWiretransferDetails(String accountDetails) {
        // Launch corresponding room activity
        if (mChatRooms.isEmpty()) {
            showNotAddedToRoom();
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        String roomId = mChatRooms.get(mChatRoomsSpinner.getSelectedIndex()).getRoomId();
        params.put(VectorRoomActivity.EXTRA_MATRIX_ID, mSession.getMyUserId());
        params.put(VectorRoomActivity.EXTRA_ROOM_ID, roomId);
        params.put(WireTransferFragment.WIRE_TRANSFER, accountDetails);

        resetWireTransferDetails();
        CommonActivityUtils.goToRoomPage(getActivity(), mSession, params);
    }

    private void resetWireTransferDetails() {
        mBankNameEditText.setText("");
        mBankAddressEditText.setText("");
        mAccountOwnerNameEditText.setText("");
        mBankRoutingNumberEditText.setText("");
        mBankAccountNumberEditText.setText("");
        if (!mChatRooms.isEmpty())
            mChatRoomsSpinner.setSelectedIndex(0);
    }

    private void showNotAddedToRoom() {
        if (null == mNotMemberAlert) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.alert_should_be_member_of_a_room_to_continue));
            mNotMemberAlert = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
        }
        if (null != mNotMemberAlert && !mNotMemberAlert.isShowing())
            mNotMemberAlert.show();
    }

    public static WireTransferFragment newInstance() {
        return new WireTransferFragment();
    }
}
