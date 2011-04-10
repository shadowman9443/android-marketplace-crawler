class PermissionsController < ApplicationController
  # POST /permission.xml
  # POST /permission.json  
  def create
    @permission = Permission.new(:name => params[:permission])

    respond_to do |format|
      if @permission.save
        format.xml  { render :xml => @permission, :status => :created, :location => @permission }
        format.json  { render :json => @permission, :status => :created, :location => @permission }        
      else
        format.xml  { render :xml => @permission.errors, :status => :unprocessable_entity }
        format.json  { render :json => @permission.errors, :status => :unprocessable_entity }        
      end
    end
  end
end
