package android.app.printerapp;

import android.app.printerapp.devices.DevicesFragment;
import android.app.printerapp.devices.DevicesListController;
import android.app.printerapp.devices.database.DatabaseController;
import android.app.printerapp.devices.printview.PrintViewFragment;
import android.app.printerapp.library.LibraryFragment;
import android.app.printerapp.library.StorageController;
import android.app.printerapp.library.detail.DetailViewFragment;
import android.app.printerapp.settings.SettingsFragment;
import android.app.printerapp.viewer.ViewerMain;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private static DevicesFragment mDevicesFragment;
	private LibraryFragment mLibraryFragment;
	private static ViewerMain mViewerFragment;
	private SettingsFragment mSettingsFragment;
	
	private static Fragment mCurrent;
	
	private static FragmentManager mManager;
	
	private static DialogController mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}

		
		//TODO List controllers
		new DatabaseController(this);
		DevicesListController.loadList(this);
		new StorageController();
		//new ViewerMain();
		
		mManager = getSupportFragmentManager();
		mDialog = new DialogController(this);
		
		
		mDevicesFragment = (DevicesFragment) getSupportFragmentManager().findFragmentByTag("Devices");
		mLibraryFragment = (LibraryFragment) getSupportFragmentManager().findFragmentByTag("Library");
		mViewerFragment = (ViewerMain) getSupportFragmentManager().findFragmentByTag("Viewer");
		mSettingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("Settings");
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		
		if (mTwoPane) {
			
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			
			//Switch between list ids
			
			FragmentTransaction mTransaction = mManager.beginTransaction();
			
			if (mCurrent!=null) {
				
				try {
					//We have to remove the Detail fragment because we're not using replace
					if (mCurrent.getTag().contains("Detail")) mTransaction.remove(mCurrent);
					else mTransaction.hide(mCurrent);
				} catch (NullPointerException e){
					
					e.printStackTrace();
				}
				
			}
					
						
			switch (Integer.valueOf(id)){
			
				case 1:{
					
					//Check if we already created the Fragment to avoid having multiple instances
					 if (getSupportFragmentManager().findFragmentByTag("Devices")==null){
						 mDevicesFragment = new DevicesFragment();
						 mTransaction.add(R.id.item_detail_container,mDevicesFragment, "Devices");	

					 }  
					  
					 mCurrent = mDevicesFragment;
					} break;
					
				case 2:{
					
					//Check if we already created the Fragment to avoid having multiple instances
					 if (getSupportFragmentManager().findFragmentByTag("Viewer")==null){
						 mViewerFragment = new ViewerMain();
						 mTransaction.add(R.id.item_detail_container,mViewerFragment, "Viewer");
							
					 } 
					 
					

					 mCurrent = mViewerFragment;
						 
					
				} break;
				
				case 3:{
					//Check if we already created the Fragment to avoid having multiple instances
					 if (getSupportFragmentManager().findFragmentByTag("Library")==null){
						 mLibraryFragment = new LibraryFragment();
						 mTransaction.add(R.id.item_detail_container,mLibraryFragment, "Library");
						 
					 } 
						 
					mCurrent = mLibraryFragment;

				} break;
				
				case 4:{
					
					
				} break;
				
				case 5:{
					//Check if we already created the Fragment to avoid having multiple instances
					 if (getSupportFragmentManager().findFragmentByTag("Settings")==null){
						 mSettingsFragment = new SettingsFragment();
						 mTransaction.add(R.id.item_detail_container,mSettingsFragment, "Settings");

					 } 
					 
					mCurrent = mSettingsFragment;

				}break;	
				
			}
			
			//Set the visibility for the viewer if we're not on the Viewer
			//TODO bugs with positioning after making it visible again
			if (mViewerFragment!=null) {
				
				if (mCurrent!=mViewerFragment){
					//Make the surface invisible
					mViewerFragment.setSurfaceVisibility(0);
				}
				else {
					 //Make the surface visible when we press
					 mViewerFragment.setSurfaceVisibility(1);
				}
				
			}
			
			if (mCurrent!=null ) mTransaction.show(mCurrent).commit();
			
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			startActivity(detailIntent);
			

		}
	}	
	
	
	//TODO find a better way
	public static void notifyAdapters(){
		
		try{
			
			if (mDevicesFragment!=null) mDevicesFragment.notifyAdapter();
			if (mCurrent!=null) if (mCurrent.getTag().equals("Detail Printer")) ((PrintViewFragment) mCurrent).refreshData();
		
		}catch (NullPointerException e){
			
			e.printStackTrace();
		}
	}
	
	//Add a new custom fragment with detailed view
	public static void showDetailView(int index){
		
		FragmentTransaction mTransaction = mManager.beginTransaction();
		DetailViewFragment detail = new DetailViewFragment();
		Bundle args = new Bundle();
	    args.putInt("index", index);
	    detail.setArguments(args);
		mTransaction.hide(mCurrent);
		mTransaction.add(R.id.item_detail_container, detail, "Detail View");
		mCurrent = detail;
		mTransaction.show(mCurrent).commit();
		
	}
	
	//Add a new custom fragment with detailed view
		public static void showPrintView(String name){
			
			FragmentTransaction mTransaction = mManager.beginTransaction();
			PrintViewFragment detail = new PrintViewFragment();
			Bundle args = new Bundle();
		    args.putString("printer", name);
		    detail.setArguments(args);
			mTransaction.hide(mCurrent);
			mTransaction.add(R.id.item_detail_container, detail, "Detail Printer");
			mCurrent = detail;
			mTransaction.show(mCurrent).commit();
			
		}
	
	//Send a fragment change request to the parent
	public static void requestOpenFile(final String path){
		ItemListFragment.performClick(1);
		
		//Handler will avoid crash
		Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				
				ViewerMain.openFile(path);
					
				}	
			});

	}

	@Override
	public void onBackPressed() {
		
		if (mCurrent == mLibraryFragment){
			
			if (!mLibraryFragment.goBack()) super.onBackPressed();
			
		} else super.onBackPressed();
		
	}
	
	public static void showDialog(String msg){
		mDialog.displayDialog(msg);
	}
	
}
