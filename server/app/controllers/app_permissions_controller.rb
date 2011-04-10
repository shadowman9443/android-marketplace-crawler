class AppPermissionsController < ApplicationController
  
  # POST /app_permissions/update_permissions.xml
  # POST /app_permissions/update_permissions.json
  def update_permissions
    app = App.find(params[:app_id])
    app.permission_ids = params[:permissions]
    
    respond_to do |format|
      if app.save!
        format.xml  { head :ok }
        format.json  { head :ok }   
      else
        format.xml  { render :xml => @app.errors, :status => :unprocessable_entity }
        format.json  { render :json => @app.errors, :status => :unprocessable_entity }        
      end
    end
  end
end