class AppsController < ApplicationController

  # GET /apps
  # GET /apps.xml
  # GET /apps.json  
  def index
    @apps = App.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @apps }
      format.json  { render :json => @apps }      
    end
  end

  # GET /apps/1
  # GET /apps/1.xml
  # GET /apps/1.json  
  def show
    @app = App.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @app }
      format.json  { render :json => @app }      
    end
  end

  # POST /apps
  # POST /apps.xml
  # POST /apps.json  
  def create
    @app = App.new(params[:app])
    @app.rating = Rating.new(params[:rating])
    
    respond_to do |format|
      if @app.save
        format.xml  { render :xml => @app, :status => :created, :location => @app, :only => [:id]}
        format.json  { render :json => @app, :status => :created, :location => @app, :only => [:id]}        
      else
        format.xml  { render :xml => @app.errors, :status => :unprocessable_entity }
        format.json  { render :json => @app.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # PUT /apps/1
  # PUT /apps/1.xml
  # PUT /apps/1.json  
  def update
    @app = App.find(params[:id])
    
    respond_to do |format|
      if @app.update_attributes(params[:app]) and @app.rating.update_attributes(params[:rating])
        format.xml  { head :ok }
        format.json  { head :ok }        
      else
        format.xml  { render :xml => @app.errors, :status => :unprocessable_entity }
        format.json  { render :json => @app.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # DELETE /apps/1
  # DELETE /apps/1.xml
  # DELETE /apps/1.json  
  def destroy
    @app = App.find(params[:id])
    @app.destroy

    respond_to do |format|
      format.html { redirect_to(apps_url) }
      format.xml  { head :ok }
      format.json  { head :ok }      
    end
  end
  
  # POST /apps/exists.xml
  # POST /apps/exists.json
  def exists
    @record = Record.new
    app = App.find_by_packageName(params[:packageName], :select => :id)
    
    if app.nil?
      @record.exists = false
    else
      @record.id = app.id
      @record.exists = true
    end
  
    respond_to do |format|
      format.xml {render :xml => @record}
      format.json {render :json => @record}      
    end
  end
  
  def ids
    @ids = App.greater_than(params[:id])
    
    respond_to do |format|
      format.xml {render :xml => @ids, :only => [:id, :appId]}
      format.json {render :json => @ids, :only => [:id, :appId]}      
    end
  end
end


