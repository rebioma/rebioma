package org.rebioma.client.maps;

import java.util.ArrayList;
import java.util.List;

import org.rebioma.client.bean.ShapeFileInfo;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

public class ShapeFileWindow extends Window {

	class KeyProvider implements ModelKeyProvider<ShapeFileInfo> {
		@Override
		public String getKey(ShapeFileInfo item) {
			String key = item.getGid() + item.getLibelle();
			return key;
		}
	}

	public ShapeFileWindow() {
		super();
		this.init();
	}

	private void init() {
		ContentPanel panel = new ContentPanel();
		panel.setHeadingText("Liste des fichiers shapes");
		panel.setPixelSize(315, 400);
		panel.addStyleName("margin-10");
		/*
		VerticalLayoutContainer con = new VerticalLayoutContainer();
		panel.add(con);

		// final ExampleServiceAsync service = GWT.create(ExampleService.class);
		RpcProxy<ShapeFileInfo, List<ShapeFileInfo>> proxy = new RpcProxy<ShapeFileInfo, List<ShapeFileInfo>>() {
			@Override
			public void load(ShapeFileInfo loadConfig,
					AsyncCallback<List<ShapeFileInfo>> callback) {
				// appel du service.
			}
		};

		TreeLoader<ShapeFileInfo> loader = new TreeLoader<ShapeFileInfo>(proxy) {
			@Override
			public boolean hasChildren(ShapeFileInfo parent) {
				return parent.getGid() == 0;
			}
		};

		TreeStore<ShapeFileInfo> store = new TreeStore<ShapeFileInfo>(
				new KeyProvider());
		loader.addLoadHandler(new ChildTreeStoreBinding<ShapeFileInfo>(store));
		final Tree<ShapeFileInfo, String> tree = new Tree<ShapeFileInfo, String>(
			store, new ValueProvider<ShapeFileInfo, String>() {
	
				@Override
				public String getValue(ShapeFileInfo object) {
					return object.getLibelle();
				}
	
				@Override
				public void setValue(ShapeFileInfo object, String value) {
				}
	
				@Override
				public String getPath() {
					return "libelle";
				}
			});
		tree.setLoader(loader);
		TreeStateHandler<ShapeFileInfo> stateHandler = new TreeStateHandler<ShapeFileInfo>(tree);
	    stateHandler.loadState();
	    //tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());
	    panel.add(tree);
		this.add(panel);*/
		
		FlowLayoutContainer con = new FlowLayoutContainer();
		 
	    TreeStore<ShapeFileInfo> store = new TreeStore<ShapeFileInfo>(new KeyProvider());
	    ShapeFileInfo root = new ShapeFileInfo();
	    root.setLibelle("Limite region madagascar");
	    store.add(root);
	    List<ShapeFileInfo> infos = new ArrayList<ShapeFileInfo>();
	    for(int i=1;i<=22;i++){
	    	ShapeFileInfo info = new ShapeFileInfo();
	    	info.setLibelle("region " + i);
	    	info.setGid(i);
	    	infos.add(info);
	    }
	    store.add(root, infos);
	    
	    final Tree<ShapeFileInfo, String> tree = new Tree<ShapeFileInfo, String>(store, new ValueProvider<ShapeFileInfo, String>() {
	    	 
		    	@Override
				public String getValue(ShapeFileInfo object) {
					return object.getLibelle();
				}
	
				@Override
				public void setValue(ShapeFileInfo object, String value) {
				}
	
				@Override
				public String getPath() {
					return "libelle";
				}
	      });
	      tree.setWidth(300);
	      //tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());
	      tree.setCheckable(true);
	      tree.setCheckStyle(CheckCascade.TRI);
	      tree.setAutoLoad(true);
	      /*tree.addCheckChangedHandler(new CheckChangedHandler<ShapeFileInfo>() {
	          @Override
	          public void onCheckChanged(CheckChangedEvent<ShapeFileInfo> event) {
	            
	          }
	      });*/
	      con.add(tree, new MarginData(10));
	      panel.add(con);
	      this.add(panel);
	}
}
