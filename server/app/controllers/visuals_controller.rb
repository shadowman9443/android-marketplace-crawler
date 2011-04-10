class VisualsController < ApplicationController

  # POST /visuals
  def create    
    @app = App.find(params[:app_id])    
    @visual = @app.visuals.new(:image => params[:image])
  
    respond_to do |format|
      if @visual.save
        format.html  { render :json => @visual, :status => :created}      
      else
        format.html  { render :json => @visual.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # PUT /visuals/1
  def update
    @app = App.find(params[:app_id])
    @visual = @app.visuals.find(params[:id])

    respond_to do |format|
      if @visual.update_attributes(:image => params[:image])
        format.html  { head :ok }
      else
        format.html  { render :json => @visual.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # DELETE /visuals/1.xml
  # DELETE /visuals/1.json
  def destroy
    @visual = Visual.find(params[:id])
    @visual.destroy
    
    respond_to do |format|
      format.xml  { head :ok }
      format.json  { head :ok }      
    end
  end
  
  # POST /visuals/exists.xml
  # POST /visuals/exists.json
  def exists
    @record = Record.new
    visual = Visual.find_by_app_id_and_image_file_name(params[:app_id], params[:filename], :select => :id)
    
    if visual.nil?
      @record.exists = false
    else
      @record.id = visual.id
      @record.exists = true
    end

    respond_to do |format|
      format.xml {render :xml => @record}
      format.json {render :json => @record}      
    end
  end

end
