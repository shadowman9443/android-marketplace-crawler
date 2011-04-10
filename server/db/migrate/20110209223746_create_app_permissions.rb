class CreateAppPermissions < ActiveRecord::Migration
  def self.up
    create_table :app_permissions do |t|
      t.references :app
      t.references :permission

      t.timestamps
    end
    add_index :app_permissions, :app_id 
    add_index :app_permissions, :permission_id
  end

  def self.down
    drop_table :app_permissions
  end
end
