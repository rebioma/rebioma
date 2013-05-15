package org.rebioma.client.gxt.treegrid;

import java.util.LinkedList;
import java.util.List;

import org.rebioma.client.bean.StatisticModel;
import org.rebioma.client.bean.StatisticModel.StatisticsModelProperties;
import org.rebioma.client.services.StatisticsService;
import org.rebioma.client.services.StatisticsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class StatisticsPanel  extends Widget{
	private final StatisticsServiceAsync statService = GWT
	.create(StatisticsService.class);
	
	//private ContentPanel root;
	public static final int NUM_PAGE = 9;
	

	public Widget statisticsPanel(String gridTitle) {
		 final StatisticsServiceAsync service = GWT.create(StatisticsService.class);
		RpcProxy<PagingLoadConfig, PagingLoadResult<StatisticModel>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<StatisticModel>>() {
		      @Override
		      public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<StatisticModel>> callback) {
		        service.getStatisticsByType(0,loadConfig, callback);
		      }
		    };
		// Generate the key provider and value provider for the Data class
		StatisticsModelProperties statisticsModelProperties = GWT.create(StatisticsModelProperties.class);
		
		
		List<ColumnConfig<StatisticModel, ?>> ccs = new LinkedList<ColumnConfig<StatisticModel, ?>>();
		ccs.add(new ColumnConfig<StatisticModel, String>(
				statisticsModelProperties.title(), 150, ""));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbPrivateData(), 80, "Private data"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbPublicData(), 80,"Public data"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbReliable(), 80, "Public Occurrences"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbAwaiting(), 100, "Awaiting review"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbQuestionable(), 80, "Questionable"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbInvalidated(), 80, "Invalidated"));
		ccs.add(new ColumnConfig<StatisticModel, Integer>(
				statisticsModelProperties.nbTotal(), 80, "All"));
		
		ColumnModel<StatisticModel> cm = new ColumnModel<StatisticModel>(
				ccs);
		
		
		
	  	ListStore<StatisticModel> store = new ListStore<StatisticModel>(new ModelKeyProvider<StatisticModel>() {
	        @Override
	        public String getKey(StatisticModel item) {
	          return "" + item.getIdKey();
	        }
	      });
	      
	  	
	  	final PagingLoader<PagingLoadConfig, PagingLoadResult<StatisticModel>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<StatisticModel>>(
	  	        proxy);
	  	    loader.setRemoteSort(true);
	  	    loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, StatisticModel, PagingLoadResult<StatisticModel>>(store));
	  	    
	  	  final PagingToolBar toolBar = new PagingToolBar(50);
	      toolBar.getElement().getStyle().setProperty("borderBottom", "none");
	      toolBar.bind(loader);
	       
	      IdentityValueProvider<StatisticModel> identity = new IdentityValueProvider<StatisticModel>();
	     /* final CheckBoxSelectionModel<StatisticModel> sm = new CheckBoxSelectionModel<StatisticModel>(identity) {
	        @Override
	        protected void onRefresh(RefreshEvent event) {
	          // this code selects all rows when paging if the header checkbox is selected
	          if (isSelectAllChecked()) {
	            selectAll();
	          }
	          super.onRefresh(event);
	        }
	      };
	     */
	  	  /*ListStore<StatisticModel> store = new ListStore<StatisticModel>(statisticsModelProperties.key());
		      store.addAll(StatisticModel.getstats());*/
	     /* root = new ContentPanel();
	      root.setHeadingText(gridTitle);
	     // root.getHeader().setIcon();
	     // root.setPixelSize(600, 300);
	      root.setWidth("100%");
	      root.addStyleName("margin-10");*/
	      
	      final Grid<StatisticModel> grid = new Grid<StatisticModel>(store, cm){
	          @Override
	          protected void onAfterFirstAttach() {
	            super.onAfterFirstAttach();
	            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	              @Override
	              public void execute() {
	                loader.load();
	              }
	            });
	          }
	        };
	        
	        grid.getView().setForceFit(true);
	        grid.setLoadMask(true);
	        grid.setLoader(loader);
	     // grid.getView().setAutoExpandColumn();
	     /* grid.getView().setStripeRows(true);
	      grid.getView().setColumnLines(true);
	      grid.setBorders(false);*/
	        
	        FramedPanel cp = new FramedPanel();
	        cp.setCollapsible(true);
	        cp.setHeadingText("gridTitle");
	        cp.setPixelSize(730, 400);
	        cp.addStyleName("margin-10");
	        
	        VerticalLayoutContainer con = new VerticalLayoutContainer();
	        con.setBorders(true);
	        con.add(grid, new VerticalLayoutData(1, 1));
	        con.add(toolBar, new VerticalLayoutData(1, -1));
	        cp.setWidget(con);
	 
	      /*grid.setColumnReordering(true);
	      grid.setStateful(true);
	      grid.setStateId("gridExample");*/
	 
	      /*GridStateHandler<StatisticModel> state = new GridStateHandler<StatisticModel>(grid);
	      state.loadState();*/
	      
	      grid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				grid.setSelectionModel(new GridSelectionModel<StatisticModel>());
				
			}
		});
	      
	     /* VerticalLayoutContainer con = new VerticalLayoutContainer();
	      root.setWidget(con);
	 
	     
	      con.add(grid, new VerticalLayoutData(1, -1));
	 
	      // needed to enable quicktips (qtitle for the heading and qtip for the
	      // content) that are setup in the change GridCellRenderer
	      new QuickTip(grid);*/
		return cp;
		
	}
	/*public  Grid<StatisticModel> getGrid(){
		return 
		
	}
	*/

}
