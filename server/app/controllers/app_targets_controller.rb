class AppTargetsController < ApplicationController

  # POST /app_targets.xml
  # POST /app_targets.json  
  def create
    @app_target = AppTarget.new(params[:app_target])

    respond_to do |format|
      if @app_target.save
        format.xml  { render :xml => @app_target, :status => :created, :location => @app_target }
        format.json  { render :json => @app_target, :status => :created, :location => @app_target }        
      else
        format.xml  { render :xml => @app_target.errors, :status => :unprocessable_entity }
        format.json  { render :json => @app_target.errors, :status => :unprocessable_entity }        
      end
    end
  end

  # DELETE /app_targets/1.xml
  # DELETE /app_targets/1.json
  def destroy
    @app_target = AppTarget.find(params[:id])
    @app_target.destroy

    respond_to do |format|
      format.xml  { head :ok }
      format.json  { head :ok }      
    end
  end
  
  # POST /app_targets/exists.xml
  # POST /app_targets/exists.json  
  def exists
    @record = Record.new
    @record.exists = AppTarget.exists?(params[:app_target])

    respond_to do |format|
      format.xml {render :xml => @record}
      format.json {render :json => @record}      
    end
  end
end
